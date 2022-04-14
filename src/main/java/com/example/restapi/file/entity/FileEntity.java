package com.example.restapi.file.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class FileEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private Long size;

    @Builder
    public FileEntity(String name, String url, Long size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }
}
