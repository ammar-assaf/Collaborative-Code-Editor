package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVersion extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CodeFile file;

    @Column(name = "version_name", nullable = false)
    private String versionName;

    @Column(name = "version_file_path", nullable = false)
    private String versionFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    @ToString.Exclude
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User modifiedBy;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public FileVersion(CodeFile file, String versionFilePath, User modifiedBy, String description, String versionName) {
        this.file = file;
        this.versionFilePath = versionFilePath;
        this.modifiedBy = modifiedBy;
        this.description = description;
        this.timestamp = new Date();
        this.versionName = versionName;
    }
}
