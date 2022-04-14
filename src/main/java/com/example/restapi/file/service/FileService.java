package com.example.restapi.file.service;

import com.example.restapi.file.entity.FileEntity;
import com.example.restapi.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public FileEntity saveFile(FileEntity file) {
        return fileRepository.save(file);
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }
}
