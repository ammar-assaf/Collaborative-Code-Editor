package com.example.backend.controller;

import com.example.backend.dto.CodeFileDTO;
import com.example.backend.dto.FileVersionDTO;
import com.example.backend.entities.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.VersionControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files/{fileId}/versions")
public class FileVersionController {

    @Autowired
    private VersionControlService versionControlService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/{versionId}/content")
    public ResponseEntity<String> getVersionContent(@PathVariable Long versionId,
            @AuthenticationPrincipal User user) {
        String content = versionControlService.getFileVersionContent(versionId, user);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<FileVersionDTO> getVersion(@PathVariable Long versionId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(versionControlService.getFileVersionById(versionId, user));
    }

    @GetMapping
    public ResponseEntity<List<FileVersionDTO>> getFileVersions(@PathVariable Long fileId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(versionControlService.getFileVersions(fileId, user));
    }

    @PostMapping("/fork")
    public ResponseEntity<CodeFileDTO> forkFile(@PathVariable Long fileId, @AuthenticationPrincipal User user) {
        CodeFileDTO forkedFile = versionControlService.forkFile(fileId, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(forkedFile);
    }

}
