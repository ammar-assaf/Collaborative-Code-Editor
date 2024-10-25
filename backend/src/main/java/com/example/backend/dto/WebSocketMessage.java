package com.example.backend.dto;

import lombok.Data;

@Data
public class WebSocketMessage {

    private Long fileId;
    private String command;
    private String filename;
    private String content;
    private String userEmail;
    private String description;
}
