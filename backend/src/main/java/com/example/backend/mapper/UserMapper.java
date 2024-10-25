package com.example.backend.mapper;

import com.example.backend.dto.UserDTO;
import com.example.backend.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);

}
