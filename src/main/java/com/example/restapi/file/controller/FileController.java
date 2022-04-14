package com.example.restapi.file.controller;

import com.example.restapi.file.entity.FileEntity;
import com.example.restapi.file.properties.FileProperties;
import com.example.restapi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final FileProperties fileProperties;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestPart List<MultipartFile> files) throws IOException {
        final String location = fileProperties.getLocation();
        final List<FileEntity> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileUrl = location + fileName;
            Long fileSize = file.getSize();
            file.transferTo(new File(fileUrl));
            fileList.add(FileEntity.builder()
                    .name(fileName)
                    .url(fileUrl)
                    .size(fileSize)
                    .build());
        }
        fileService.saveFileList(fileList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
        Resource resource = fileService.loadFile(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping
    public ResponseEntity<?> getFiles() {
        List<FileEntity> files = fileService.findAll();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        String url = fileService.findById(id).getUrl();
        fileService.deleteById(id);
        File file = new File(url);
        if (file.exists()) {
            if (file.delete()) {
                log.info("파일삭제 성공");
            } else {
                log.info("파일삭제 실패");
            }
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
