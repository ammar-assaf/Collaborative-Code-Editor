package com.example.backend.repository;

import com.example.backend.entities.Comment;
import com.example.backend.entities.FileVersion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVersion(FileVersion version);
}
