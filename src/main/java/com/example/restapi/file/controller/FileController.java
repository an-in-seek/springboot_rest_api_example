package com.example.restapi.file.controller;

import com.example.restapi.file.entity.FileEntity;
import com.example.restapi.file.properties.FileProperties;
import com.example.restapi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final FileProperties fileProperties;

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestPart List<MultipartFile> files) throws IOException {
        final List<FileEntity> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileUrl = fileProperties.getLocation() + fileName;
            Long fileSize = file.getSize();
            file.transferTo(Paths.get(fileUrl));
            fileList.add(FileEntity.builder()
                    .name(fileName)
                    .url(fileUrl)
                    .size(fileSize)
                    .build());
        }
        fileService.saveFileList(fileList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 파일 다운로드
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        Resource resource = fileService.loadFile(fileName);
        String absolutePath = resource.getFile().getAbsolutePath();
        String contentType = request.getServletContext().getMimeType(absolutePath);
        if (!StringUtils.hasText(contentType)) contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // 파일 리스트 조회
    @GetMapping
    public ResponseEntity<?> getFiles() {
        List<FileEntity> files = fileService.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    // ID로 파일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) throws IOException {
        String url = fileService.findById(id).getUrl();
        Path path = Paths.get(url);
        Files.delete(path);
        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
