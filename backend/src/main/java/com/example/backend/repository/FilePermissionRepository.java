package com.example.backend.repository;

import com.example.backend.entities.CodeFile;
import com.example.backend.entities.FilePermission;
import com.example.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilePermissionRepository extends JpaRepository<FilePermission, Long> {
    @Query("select fp.file from FilePermission fp where fp.user = ?1")
    List<CodeFile> getCodeFilesByUser(User user);
}
