package com.example.backend.utils.defaultContent;

public class JavaDefaultCodeProvider implements DefaultCodeProvider {

    @Override
    public String getDefaultCode(String filename) {
        return """
            public class %s {
                public static void main(String[] args) {
                    System.out.println("Hello, Java!");
                }
            }
            """.formatted(filename);

    }
}
