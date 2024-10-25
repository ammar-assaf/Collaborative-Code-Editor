package com.example.backend.controller;

import com.example.backend.dto.CommentDTO;
import com.example.backend.entities.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files/{versionId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Void> addComment(@PathVariable Long versionId,
            @Valid @RequestBody CommentDTO commentDTO,
            @AuthenticationPrincipal User user) {
        commentService.addComment(commentDTO, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Long versionId) {
        return ResponseEntity.ok(commentService.getComments(versionId));
    }
}
