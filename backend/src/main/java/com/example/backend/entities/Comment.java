package com.example.backend.entities;

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
public class Comment extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FileVersion version;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int lineNumber;

    @Column(nullable = false)
    private Date timestamp;

}
