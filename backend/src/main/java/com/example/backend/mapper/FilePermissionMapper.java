package com.example.backend.mapper;

import com.example.backend.dto.FilePermissionDTO;
import com.example.backend.entities.FilePermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface FilePermissionMapper {
    FilePermissionDTO filePermissionToFilePermissionDTO(FilePermission filePermission);
}
