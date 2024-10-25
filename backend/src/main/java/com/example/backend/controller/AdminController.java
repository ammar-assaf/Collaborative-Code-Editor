package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.PageDTO;
import com.example.backend.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageDTO<UserDTO>> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page) {

        PageDTO<UserDTO> users = userService.getUsers(page);
        return ResponseEntity.ok(users);
    }

}
