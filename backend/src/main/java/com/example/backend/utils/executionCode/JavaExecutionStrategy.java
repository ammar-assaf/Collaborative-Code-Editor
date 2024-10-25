package com.example.backend.utils.executionCode;

import com.example.backend.entities.CodeFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaExecutionStrategy implements ExecutionStrategy {
    @Override
    public String compileAndRun(CodeFile file, String code) throws Exception {
        String fileName = "%s.java".formatted(file.getFilename());
        Files.write(Paths.get(fileName), code.getBytes());

        ProcessBuilder processBuilder = new ProcessBuilder(
                "docker", "run", "--rm", "-v", "%s:/usr/src/app".formatted(System.getProperty("user.dir")),
                "openjdk:latest", "bash", "-c",
                "javac /usr/src/app/%s && java -cp /usr/src/app %s".formatted(fileName, file.getFilename()));

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        Files.deleteIfExists(Paths.get(fileName));

        return output;
    }
}
