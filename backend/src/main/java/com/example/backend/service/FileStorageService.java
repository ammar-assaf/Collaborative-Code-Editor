package com.example.backend.service;

import com.example.backend.entities.User;
import com.example.backend.exception.StorageException;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService implements FileStorage {

    private final Path rootLocation;

    public FileStorageService() {
        this.rootLocation = Paths.get("code-editor-files");
    }

    @PostConstruct
    public void init() {
        createDirectories(rootLocation);
    }

    @Override
    public Path cloneFile(User owner, String originalFilePath, String originalFilename, boolean isVersion) {
        Path originalPath = Paths.get(originalFilePath);
        Path userDir = createUserDirectory(owner);

        Path targetDir;
        if (isVersion) {
            targetDir = userDir.resolve(originalFilename).resolve("%s_versions".formatted(originalFilename));
        } else {
            targetDir = userDir.resolve(originalFilename);
        }

        createDirectories(targetDir);

        Path newFilePath = targetDir.resolve(originalPath.getFileName());
        copyFile(originalPath, newFilePath);
        return newFilePath;
    }

    @Override
    public Path saveFile(User owner, String filename, String content) {
        Path userDir = createUserDirectory(owner);
        Path fileDir = userDir.resolve(filename);
        createDirectories(fileDir);
        Path filePath = fileDir.resolve(filename);
        writeFile(filePath, content);
        return filePath;
    }

    @Override
    public String readFile(String filePath) {
        return readFromFile(Paths.get(filePath));
    }

    @Override
    public void deleteFile(String filePath) {
        deleteIfExists(Paths.get(filePath));
    }

    @Override
    public Path saveFileVersion(User owner, String filename, String content, int version) {
        Path userDir = createUserDirectory(owner);
        Path versionDir = userDir.resolve(filename).resolve("%s_versions".formatted(filename));
        createDirectories(versionDir);

        Path versionFilePath = versionDir.resolve("%s_v%d".formatted(filename, version));
        writeFile(versionFilePath, content);
        return versionFilePath;
    }

    private Path createUserDirectory(User owner) {
        return rootLocation.resolve("%s_%d".formatted(owner.getUsername(), owner.getId()));
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new StorageException("Could not create directory: %s".formatted(path), e);
        }
    }

    private void copyFile(Path source, Path target) throws StorageException {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Could not copy file from %s to %s".formatted(source, target), e);
        }
    }

    private void writeFile(Path filePath, String content) {
        try {
            Files.write(filePath, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Could not write file: %s".formatted(filePath), e);
        }
    }

    private String readFromFile(Path filePath) {
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new StorageException("Could not read file: %s".formatted(filePath), e);
        }
    }

    private void deleteIfExists(Path filePath) {
        try {
            Path parentDir = filePath.getParent();
            Files.deleteIfExists(filePath);
            if (parentDir != null && isDirectoryEmpty(parentDir)) {
                Files.deleteIfExists(parentDir);
            }
        } catch (IOException e) {
            throw new StorageException("Could not delete file: %s".formatted(filePath), e);
        }
    }

    private boolean isDirectoryEmpty(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            return !stream.iterator().hasNext();
        }
    }

}
