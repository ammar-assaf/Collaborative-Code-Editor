package com.example.backend.utils.executionCode;

import org.springframework.stereotype.Component;

@Component
public class ExecutionStrategyFactory {

    public ExecutionStrategy getExecutionStrategy(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> new JavaExecutionStrategy();
            case "python" -> new PythonExecutionStrategy();
            case "javascript" -> new JavaScriptExecutionStrategy();
            default -> throw new UnsupportedOperationException("Unsupported language: %s".formatted(language));
        };
    }
}
