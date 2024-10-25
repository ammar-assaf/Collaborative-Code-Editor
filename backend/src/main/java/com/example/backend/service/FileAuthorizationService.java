package com.example.backend.service;

import com.example.backend.entities.CodeFile;
import com.example.backend.enums.Permission;
import com.example.backend.entities.User;
import org.springframework.stereotype.Service;

@Service
public class FileAuthorizationService {

    public boolean canAccess(Long fileId, User user) {
        return user.getAccessFile().stream().anyMatch(file -> file.getFile().getId().equals(fileId));
    }

    public boolean canEdit(User user, CodeFile file) {
        return file.getOwner().getEmail().equals(user.getEmail()) || file.getSharedWith().stream()
                .anyMatch(filePermission -> filePermission.getUser().getEmail().equals(user.getEmail())
                        && filePermission.getPermission().equals(Permission.EDIT));
    }

    public boolean fullAccess(User user, CodeFile file) {
        return file.getOwner().getEmail().equals(user.getEmail());
    }

}
