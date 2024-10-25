package com.example.backend.controller;

import com.example.backend.dto.CodeFileDTO;
import com.example.backend.dto.FilePermissionDTO;
import com.example.backend.entities.User;
import com.example.backend.service.FileService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Object> createFile(@RequestParam String filename,
            @RequestParam String language,
            @AuthenticationPrincipal User user) {
        CodeFileDTO file = fileService.createFile(filename, user, language);
        return ResponseEntity.status(HttpStatus.CREATED).body(file);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<CodeFileDTO> getFileById(@PathVariable Long fileId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok(fileService.getFileById(fileId, userService.findByEmail(user.getEmail()).orElse(null)));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId,
            @AuthenticationPrincipal User user) {
        fileService.deleteFile(fileId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fileId}/content")
    public ResponseEntity<String> getFileContent(@PathVariable Long fileId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity
                .ok(fileService.getFileContent(fileId, userService.findByEmail(user.getEmail()).orElse(null)));
    }

    @PostMapping("/add-permission/{id}")
    public ResponseEntity<FilePermissionDTO> addUserPermission(@PathVariable Long id,
            @RequestBody FilePermissionDTO filePermissionDTO,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(fileService.addUserPermission(id, filePermissionDTO, user));
    }

    @PutMapping("/{fileId}/content")
    public ResponseEntity<Void> updateFileContent(@PathVariable Long fileId,
            @RequestBody String content,
            @AuthenticationPrincipal User user) {
        fileService.updateFile(fileId, content, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CodeFileDTO>> getUserFiles(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(fileService.getFilesByOwner(user));
    }

}
