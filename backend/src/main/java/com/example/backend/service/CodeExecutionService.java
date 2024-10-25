package com.example.backend.service;

import com.example.backend.entities.CodeFile;
import com.example.backend.utils.executionCode.ExecutionStrategy;
import com.example.backend.utils.executionCode.ExecutionStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {

    @Autowired
    private ExecutionStrategyFactory executionStrategyFactory;

    public String compileAndRun(CodeFile file, String code) throws Exception {
        ExecutionStrategy executionStrategy = executionStrategyFactory.getExecutionStrategy(file.getLanguage());
        return executionStrategy.compileAndRun(file, code);
    }
}
