package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "filename", "owner_id" }))
public class CodeFile extends AbstractEntity {

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<FileVersion> versions = new ArrayList<>();

    @Column(name = "forked_from")
    private Long forkedFrom;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    @JsonManagedReference("filePermissions")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<FilePermission> sharedWith;

    public CodeFile(String filename, User owner, String filePath, String language) {
        this.filename = filename;
        this.owner = owner;
        this.filePath = filePath;
        this.language = language;
        this.createdAt = new Date();
        this.sharedWith = new ArrayList<>();
    }

    public void addVersion(FileVersion version) {
        this.versions.add(version);
    }

    public void addPermission(FilePermission permission) {
        this.sharedWith.add(permission);
    }

}
