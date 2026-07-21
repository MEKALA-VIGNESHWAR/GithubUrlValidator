package com.example.demo.controller;

import com.example.demo.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.storeFile(file);

        Map<String, String> response = new HashMap<>();
        response.put("fileUrl", fileUrl);
        response.put("fileName", file.getOriginalFilename());
        response.put("contentType", file.getContentType());
        response.put("message", "File uploaded successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
