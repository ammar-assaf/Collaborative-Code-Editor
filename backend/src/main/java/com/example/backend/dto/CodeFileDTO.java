package com.example.backend.dto;

import lombok.*;

import java.util.List;

@Data
public class CodeFileDTO {
    private Long id;
    private UserDTO owner;
    private String filename;
    private String language;

    private List<FilePermissionDTO> sharedWith;

}
