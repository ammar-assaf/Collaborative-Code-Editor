package com.example.backend.repository;

import com.example.backend.entities.CodeFile;
import com.example.backend.entities.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionControlRepository extends JpaRepository<FileVersion, Long> {
    List<FileVersion> findByFileOrderByTimestampDesc(CodeFile file);

}
