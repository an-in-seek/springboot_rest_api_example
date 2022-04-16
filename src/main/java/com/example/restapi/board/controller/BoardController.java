package com.example.restapi.board.controller;

import com.example.restapi.board.entity.Board;
import com.example.restapi.board.service.BoardService;
import com.example.restapi.excel.service.ExcelWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response) throws Throwable {
        List<Board> boardList = boardService.findAll();
        ExcelWriter<Board> excelWriter = new ExcelWriter(boardList, Board.class);
        excelWriter.createExcel(response);
    }
}
