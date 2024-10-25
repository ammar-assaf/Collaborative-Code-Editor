package com.example.backend.utils.defaultContent;

public class PythonDefaultCodeProvider implements DefaultCodeProvider {

    @Override
    public String getDefaultCode(String filename) {
        return """
            if __name__ == "__main__":
                print("Hello, Python!")
            """;
    }
}
