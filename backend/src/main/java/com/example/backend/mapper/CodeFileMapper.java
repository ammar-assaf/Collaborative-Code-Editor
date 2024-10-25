package com.example.backend.mapper;

import com.example.backend.dto.CodeFileDTO;
import com.example.backend.entities.CodeFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { FilePermissionMapper.class, UserMapper.class })
public interface CodeFileMapper {

    CodeFileDTO toDto(CodeFile file);
}
