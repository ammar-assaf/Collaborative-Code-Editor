package com.example.backend.service;

import com.example.backend.dto.CodeFileDTO;
import com.example.backend.dto.FilePermissionDTO;
import com.example.backend.entities.CodeFile;
import com.example.backend.entities.FilePermission;
import com.example.backend.entities.FileVersion;
import com.example.backend.enums.Permission;
import com.example.backend.entities.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorizedUserException;
import com.example.backend.mapper.CodeFileMapper;
import com.example.backend.repository.CodeFileRepository;
import com.example.backend.repository.FilePermissionRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.utils.FileLockingUtils;
import com.example.backend.utils.defaultContent.DefaultCodeProvider;
import com.example.backend.utils.defaultContent.DefaultCodeProviderFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private CodeFileRepository codeFileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FilePermissionRepository filePermissionRepository;

    @Autowired
    private FileAuthorizationService fileAuthorizationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CodeFileMapper codeFileMapper;

    @Autowired
    private DefaultCodeProviderFactory codeProviderFactory;

    @Transactional
    public CodeFileDTO createFile(String filename, User owner, String language) throws DataIntegrityViolationException {
        DefaultCodeProvider codeProvider = codeProviderFactory.getCodeProvider(language);
        String defaultCode = codeProvider.getDefaultCode(filename);

        Path filePath = fileStorageService.saveFile(owner, filename, defaultCode);

        CodeFile file = codeFileRepository.save(new CodeFile(filename, owner, filePath.toString(), language));

        filePermissionRepository.save(new FilePermission(owner, file, Permission.OWNER));

        return codeFileMapper.toDto(file);
    }

    public CodeFile saveFile(CodeFile file) {
        return codeFileRepository.save(file);
    }

    public CodeFileDTO getFileById(Long id, User user) {
        if (!fileAuthorizationService.canAccess(id, user)) {
            throw new UnauthorizedUserException("You are not authorized to access this file");
        }
        CodeFile file = codeFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));

        return codeFileMapper.toDto(file);
    }

    public CodeFile findFileById(Long id) {
        return codeFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
    }

    public String getFileContent(Long fileId, User user) {
        if (!fileAuthorizationService.canAccess(fileId, user)) {
            throw new UnauthorizedUserException("You are not authorized to access this file");
        }
        CodeFile file = codeFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found"));
        return fileStorageService.readFile(file.getFilePath());
    }

    @Transactional
    public FilePermissionDTO addUserPermission(Long id, FilePermissionDTO filePermissionDTO, User owner) {
        CodeFile file = codeFileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        User user = userRepository.findByEmail(filePermissionDTO.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!fileAuthorizationService.fullAccess(owner, file)) {
            throw new UnauthorizedUserException("You are not authorized to share this file");
        }
        validatePermission(filePermissionDTO);
        FilePermission filePermission = FilePermission.builder()
                .user(user)
                .file(file).permission(filePermissionDTO.getPermission()).build();
        filePermissionRepository.save(filePermission);
        return filePermissionDTO;
    }

    @Transactional
    public void updateFile(Long fileId, String content, User user) {

        FileLockingUtils.lockFile(fileId);
        try {
            CodeFile file = codeFileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found"));

            if (!fileAuthorizationService.canEdit(user, file)) {
                throw new UnauthorizedUserException("You are not authorized to edit this file");
            }
            fileStorageService.saveFile(file.getOwner(), file.getFilename(), content);

            codeFileRepository.save(file);
        } finally {
            FileLockingUtils.unlockFile(fileId);
        }
    }

    public List<CodeFileDTO> getFilesByOwner(User owner) {
        List<CodeFile> files = codeFileRepository.findByEmail(owner.getEmail());
        return files.stream().map(file -> codeFileMapper.toDto(file))
                .collect(Collectors.toList());
    }

    public List<CodeFile> getFilesByUser(User user) {
        return filePermissionRepository.getCodeFilesByUser(user);
    }

    @Transactional
    public void deleteFile(Long id, User user) {
        FileLockingUtils.lockFile(id);
        try {
            CodeFile file = codeFileRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("File not found"));
            if (!fileAuthorizationService.fullAccess(user, file)) {
                throw new UnauthorizedUserException("You are not authorized to delete this file");
            }

            codeFileRepository.delete(file);
            for (FileVersion version : file.getVersions()) {
                fileStorageService.deleteFile(version.getVersionFilePath());
            }
            fileStorageService.deleteFile(file.getFilePath());

        } finally {
            FileLockingUtils.unlockFile(id);
        }
    }

    private void validatePermission(FilePermissionDTO filePermissionDTO) {
        int permission = filePermissionDTO.getPermission().ordinal();
        if (permission < 0 || permission > 2) {
            throw new IllegalArgumentException("Invalid permission value. It should be 0, 1, or 2.");
        }
    }

}
