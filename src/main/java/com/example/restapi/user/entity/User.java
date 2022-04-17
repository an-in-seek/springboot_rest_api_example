package com.example.restapi.user.entity;

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
@ExcelFileName(fileName = "사용자목록")
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelColumnName(headerName = "아이디")
    private Long id;

    @Column
    @ExcelColumnName(headerName = "이름")
    private String name;

    @Column
    @ExcelColumnName(headerName = "이메일")
    private String email;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
