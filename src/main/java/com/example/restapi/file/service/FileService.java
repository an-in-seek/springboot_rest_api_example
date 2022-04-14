package com.example.restapi.file.service;

import com.example.restapi.file.properties.FileProperties;
import com.example.restapi.file.entity.FileEntity;
import com.example.restapi.file.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final Path fileStorageLocation;

    public FileService(FileRepository fileRepository, FileProperties fileUploadProperties) {
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
    }

    public List<FileEntity> saveFileList(List<FileEntity> fileList) {
        return fileRepository.saveAll(fileList);
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }

    public FileEntity findById(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteById(Long id) {
        fileRepository.deleteById(id);
    }

    public Resource loadFile(String fileName) throws FileNotFoundException {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }


}
