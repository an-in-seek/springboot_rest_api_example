package com.example.restapi.user.controller;

import com.example.restapi.excel.service.ExcelService;
import com.example.restapi.user.entity.User;
import com.example.restapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response) throws Throwable {
        List<User> userList = userService.findAll();
        ExcelService<User> excelService = new ExcelService(userList, User.class);
        excelService.downloadExcel(response);
    }
}
