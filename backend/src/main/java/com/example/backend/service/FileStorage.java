package com.example.backend.service;

import com.example.backend.entities.User;

import java.nio.file.Path;

public interface FileStorage {
    Path cloneFile(User owner, String originalFilePath, String originalFilename, boolean isVersion);

    Path saveFile(User owner, String filename, String content);

    String readFile(String filePath);

    void deleteFile(String filePath);

    Path saveFileVersion(User owner, String filename, String content, int version);

}
