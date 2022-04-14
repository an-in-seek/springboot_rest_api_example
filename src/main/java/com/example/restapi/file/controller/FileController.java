package com.example.restapi.file.controller;

import com.example.restapi.file.entity.FileEntity;
import com.example.restapi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final String filePath = "D:/upload/";

    @PostMapping
    public ResponseEntity<?> uploadFiles(@RequestPart List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            // 변수 세팅
            String originFileName = file.getOriginalFilename();
            String fileUrl = filePath + originFileName;
            Long fileSize = file.getSize();

            // 경로에 파일 저장
            file.transferTo(new File(filePath + originFileName));

            // 파일 저장
            fileService.saveFile(FileEntity.builder()
                    .name(originFileName)
                    .url(fileUrl)
                    .size(fileSize)
                    .build());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getFiles(){
        List<FileEntity> files = fileService.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
