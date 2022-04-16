package com.example.restapi.board.entity;

import com.example.restapi.excel.annotation.ExcelColumnName;
import com.example.restapi.excel.annotation.ExcelFileName;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ExcelFileName(fileName = "게시글목록")
public class Board {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelColumnName(headerName = "아이디")
    private Long id;

    @Column
    @ExcelColumnName(headerName = "제목")
    private String title;

    @Column
    @ExcelColumnName(headerName = "내용")
    private String content;

    @Builder
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
