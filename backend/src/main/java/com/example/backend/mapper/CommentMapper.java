package com.example.backend.mapper;

import org.mapstruct.Mapper;

import com.example.backend.dto.CommentDTO;
import com.example.backend.entities.Comment;

@Mapper(componentModel = "spring", uses = { UserMapper.class, FileVersionMapper.class })
public interface CommentMapper {

    CommentDTO commentToCommentDTO(Comment comment);

}