package com.example.backend.repository;

import com.example.backend.entities.CodeFile;
import com.example.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeFileRepository extends JpaRepository<CodeFile, Long> {
    List<CodeFile> findByOwner(User owner);

    @Query("select f from CodeFile f where f.owner.email = ?1 or f in (select fp.file from FilePermission fp where fp.user.email = ?1)")
    List<CodeFile> findByEmail(String email);
}
