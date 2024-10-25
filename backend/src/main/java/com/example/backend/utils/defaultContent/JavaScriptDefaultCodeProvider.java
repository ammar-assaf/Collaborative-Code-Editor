package com.example.backend.utils.defaultContent;


public class JavaScriptDefaultCodeProvider implements DefaultCodeProvider {
    @Override
    public String getDefaultCode(String filename) {
        return "console.log('Hello, JavaScript!');";
    }
}
