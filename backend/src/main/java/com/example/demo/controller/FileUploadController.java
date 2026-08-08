package com.example.demo.controller;

import com.example.demo.service.FileStorageService;
import com.example.demo.service.PresignedUrlService;
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
    private final PresignedUrlService presignedUrlService;

    public FileUploadController(FileStorageService fileStorageService, PresignedUrlService presignedUrlService) {
        this.fileStorageService = fileStorageService;
        this.presignedUrlService = presignedUrlService;
    }

    @GetMapping("/presigned-url")
    public ResponseEntity<?> getPresignedUploadUrl(
            @RequestParam String fileName,
            @RequestParam(required = false, defaultValue = "application/octet-stream") String contentType) {
        Map<String, Object> result = presignedUrlService.generatePresignedUploadUrl(fileName, contentType);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/confirm-upload")
    public ResponseEntity<?> confirmUpload(@RequestBody Map<String, String> body) {
        String s3Key = body.get("s3Key");
        String fileName = body.get("fileName");
        if (s3Key == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "s3Key is required"));
        }
        Map<String, Object> result = presignedUrlService.confirmUpload(s3Key, fileName);
        return ResponseEntity.ok(result);
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
