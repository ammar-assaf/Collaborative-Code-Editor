package com.example.backend.utils.executionCode;

import com.example.backend.entities.CodeFile;

public interface ExecutionStrategy {

    String compileAndRun(CodeFile file, String code) throws Exception;
}

