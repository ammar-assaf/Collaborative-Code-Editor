package com.example.backend.dto;

import lombok.Data;

@Data
public class FileVersionDTO {

    private Long id;
    private String versionName;
    private CodeFileDTO file;
    private UserDTO modifiedBy;
    private String description;
    private String timestamp;

}
