package com.example.backend.mapper;

import org.mapstruct.Mapper;

import com.example.backend.dto.FileVersionDTO;
import com.example.backend.entities.FileVersion;

@Mapper(componentModel = "spring", uses = { CodeFileMapper.class, UserMapper.class })
public interface FileVersionMapper {
    FileVersionDTO toDto(FileVersion fileVersion);
}
