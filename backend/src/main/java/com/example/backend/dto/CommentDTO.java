package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {
    private FileVersionDTO version;
    private UserDTO author;

    @NotBlank(message = "Content is required.")
    private String content;

    @NotNull(message = "Line number is required.")
    private int lineNumber;
    private String timestamp;
}
