package com.example.backend.service;

import com.example.backend.dto.CommentDTO;
import com.example.backend.entities.Comment;
import com.example.backend.entities.FileVersion;
import com.example.backend.entities.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.VersionControlRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    VersionControlRepository versionControlRepository;
    @Autowired
    private CommentMapper commentMapper;

    public void addComment(CommentDTO commentDTO, User author) {
        FileVersion fileVersion = versionControlRepository.findById(commentDTO.getVersion().getId()).get();
        Comment comment = new Comment();
        comment.setVersion(fileVersion);
        comment.setAuthor(author);
        comment.setContent(commentDTO.getContent());
        comment.setLineNumber(commentDTO.getLineNumber());
        comment.setTimestamp(new Date());
        commentRepository.save(comment);
    }

    public List<CommentDTO> getComments(Long versionId) {
        FileVersion fileVersion = versionControlRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));

        List<Comment> comments = commentRepository.findByVersion(fileVersion);
        return comments.stream().map(comment -> commentMapper.commentToCommentDTO(comment))
                .collect(Collectors.toList());

    }
}
