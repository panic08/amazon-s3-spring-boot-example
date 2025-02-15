package ru.panic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "post_files_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
