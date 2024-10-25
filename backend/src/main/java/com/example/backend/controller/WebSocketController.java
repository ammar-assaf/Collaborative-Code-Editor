package com.example.backend.controller;

import com.example.backend.entities.CodeFile;
import com.example.backend.entities.User;
import com.example.backend.dto.WebSocketMessage;
import com.example.backend.service.CodeExecutionService;
import com.example.backend.service.FileService;
import com.example.backend.service.UserService;
import com.example.backend.service.VersionControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FileService fileService;

    @Autowired
    private VersionControlService versionControlService;

    @Autowired
    private CodeExecutionService codeExecutionService;

    @Autowired
    private UserService userService;

    @MessageMapping("/files/{fileId}")
    public void handleWebSocketMessage(@DestinationVariable Long fileId,
            WebSocketMessage message) throws Exception {
        String command = message.getCommand();
        User user = userService.findByEmail(message.getUserEmail()).get();

        switch (command) {
            case "EDIT":
                handleEditCommand(fileId, message, user);
                break;
            case "SAVE":
                handleSaveCommand(fileId, message, user);
                break;
            case "EXECUTE":
                message.setContent(handleExecuteCommand(fileId, message));
                break;
            default:
                throw new IllegalArgumentException("Unknown command: %d".formatted(fileId));
        }

        messagingTemplate.convertAndSend("/topic/files/%d".formatted(fileId), message);
    }

    private void handleEditCommand(Long fileId, WebSocketMessage message, User user) {
        fileService.updateFile(fileId, message.getContent(), user);
    }

    private void handleSaveCommand(Long fileId, WebSocketMessage message, User user) {
        versionControlService.saveNewVersion(fileId, message.getContent(), user, message.getDescription());
    }

    private String handleExecuteCommand(Long fileId, WebSocketMessage message)
            throws Exception {
        CodeFile file = fileService.findFileById(fileId);
        return codeExecutionService.compileAndRun(file, message.getContent());
       
    }
}
