package com.example.backend.dto;

import com.example.backend.enums.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class FilePermissionDTO {
    @NotNull(message = "User is required")
    @JsonProperty(required = true)
    private UserDTO user;
    @NotNull(message = "Permission is required")
    @JsonProperty(required = true)
    private Permission permission;
}
