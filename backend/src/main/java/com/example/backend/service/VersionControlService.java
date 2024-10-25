package com.example.backend.service;

import com.example.backend.dto.CodeFileDTO;
import com.example.backend.dto.FileVersionDTO;
import com.example.backend.entities.CodeFile;
import com.example.backend.entities.FilePermission;
import com.example.backend.entities.FileVersion;
import com.example.backend.entities.User;
import com.example.backend.enums.Permission;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorizedUserException;
import com.example.backend.mapper.CodeFileMapper;
import com.example.backend.mapper.FileVersionMapper;
import com.example.backend.repository.FilePermissionRepository;
import com.example.backend.repository.VersionControlRepository;
import com.example.backend.utils.FileLockingUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VersionControlService {

    @Autowired
    private VersionControlRepository versionControlRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileAuthorizationService fileAuthorizationService;

    @Autowired
    private FilePermissionRepository filePermissionRepository;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private FileVersionMapper fileVersionMapper;

    @Transactional
    public void saveNewVersion(Long fileId, String content, User modifiedBy, String description) {
        FileLockingUtils.lockFile(fileId);
        try {
            CodeFile file = fileService.findFileById(fileId);
            if (!fileAuthorizationService.canEdit(modifiedBy, file)) {
                throw new UnauthorizedUserException("You are not authorized to save this file");
            }

            int newVersionNumber = file.getVersions().size() + 1;
            Path versionFilePath = fileStorageService.saveFileVersion(file.getOwner(),
                    file.getFilename(),
                    content,
                    newVersionNumber);

            FileVersion version = new FileVersion(file, versionFilePath.toString(), modifiedBy, description,
                    versionFilePath.getFileName().toString());
            versionControlRepository.save(version);
        } finally {
            FileLockingUtils.unlockFile(fileId);
        }
    }

    public String getFileVersionContent(Long versionId, User user) {
        FileVersion version = versionControlRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));

        if (!fileAuthorizationService.canAccess(version.getFile().getId(), user)) {
            throw new UnauthorizedUserException("You are not authorized to access this file");
        }
        return fileStorageService.readFile(version.getVersionFilePath());
    }

    public List<FileVersionDTO> getFileVersions(Long fileId, User user) {
        CodeFile file = fileService.findFileById(fileId);

        if (!fileAuthorizationService.canAccess(file.getId(), user)) {
            throw new UnauthorizedUserException("You are not authorized to access this file");
        }
        List<FileVersion> versions = versionControlRepository.findByFileOrderByTimestampDesc(file);
        return versions.stream()
                .map(version -> fileVersionMapper.toDto(version))
                .collect(Collectors.toList());
    }

    @Transactional
    public CodeFileDTO forkFile(Long fileId, User user) {
        CodeFile originalFile = fileService.findFileById(fileId);

        CodeFile forkedFile = new CodeFile();
        forkedFile.setFilename(originalFile.getFilename());
        forkedFile.setOwner(user);
        forkedFile.setLanguage(originalFile.getLanguage());
        forkedFile.setForkedFrom(originalFile.getId());
        forkedFile.setCreatedAt(new Date());

        Path forkedFilePath = fileStorageService.cloneFile(user, originalFile.getFilePath(), originalFile.getFilename(),
                false);
        forkedFile.setFilePath(forkedFilePath.toString());

        CodeFile savedFile = fileService.saveFile(forkedFile);
        filePermissionRepository.save(new FilePermission(user, savedFile, Permission.OWNER));

        for (FileVersion originalVersion : originalFile.getVersions()) {
            Path forkedVersionPath = fileStorageService.cloneFile(user, originalVersion.getVersionFilePath(),
                    originalFile.getFilename(), true);

            FileVersion forkedVersion = new FileVersion(forkedFile, forkedVersionPath.toString(),
                    originalVersion.getModifiedBy(),
                    originalVersion.getDescription(), forkedFilePath.getFileName().toString());
            forkedVersion.setTimestamp(originalVersion.getTimestamp());
            versionControlRepository.save(forkedVersion);
        }

        return codeFileMapper.toDto(savedFile);
    }

    public FileVersionDTO getFileVersionById(Long versionId, User user) {

        FileVersion fileVersion = versionControlRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));
        if (!fileAuthorizationService.canAccess(fileVersion.getFile().getId(), user)) {
            throw new UnauthorizedUserException("You are not authorized to access this file");
        }

        return fileVersionMapper.toDto(fileVersion);
    }

}
