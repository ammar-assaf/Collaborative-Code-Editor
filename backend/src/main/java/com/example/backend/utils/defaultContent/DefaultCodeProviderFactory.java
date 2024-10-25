package com.example.backend.utils.defaultContent;

import org.springframework.stereotype.Component;

@Component
public class DefaultCodeProviderFactory {

    public DefaultCodeProvider getCodeProvider(String language) {
        return switch (language.toLowerCase()) {
            case "java" -> new JavaDefaultCodeProvider();
            case "python" -> new PythonDefaultCodeProvider();
            case "javascript" -> new JavaScriptDefaultCodeProvider();
            default -> throw new UnsupportedOperationException("Language not supported: " + language);
        };
    }
}
