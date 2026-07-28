package com.example.demo.service.impl;

import com.example.demo.exception.InvalidSubmissionException;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final long MAX_FILE_SIZE = 15 * 1024 * 1024; // 15 MB cap
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".pdf", ".zip", ".png", ".jpg", ".jpeg", ".mp4", ".tar.gz", ".gz", ".docx"
    );

    @Value("${app.s3.bucket:hackforge-storage}")
    private String s3Bucket;

    @Value("${app.s3.region:us-east-1}")
    private String s3Region;

    @Value("${app.s3.endpoint:}")
    private String s3Endpoint;

    private Path fileStorageLocation;

    public FileStorageServiceImpl() {
        Path location = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(location);
            this.fileStorageLocation = location;
        } catch (Exception ex) {
            try {
                location = Paths.get(System.getProperty("java.io.tmpdir"), "uploads").toAbsolutePath().normalize();
                Files.createDirectories(location);
                this.fileStorageLocation = location;
            } catch (Exception fallbackEx) {
                throw new RuntimeException("Could not create the directory where uploaded files will be stored.", ex);
            }
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidSubmissionException("Uploaded file cannot be empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidSubmissionException("File size exceeds maximum allowed limit of 15MB.");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        
        if (originalFileName.contains("..")) {
            throw new InvalidSubmissionException("Filename contains invalid path sequence: " + originalFileName);
        }

        String extension = "";
        int extIdx = originalFileName.lastIndexOf('.');
        if (extIdx > 0) {
            extension = originalFileName.substring(extIdx).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidSubmissionException("File type '" + extension + "' is not permitted.");
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;

        // Try S3 upload if endpoint or AWS is configured
        if (s3Endpoint != null && !s3Endpoint.isBlank() && !s3Endpoint.contains("localhost")) {
            try {
                S3Client s3Client = S3Client.builder()
                        .region(Region.of(s3Region))
                        .endpointOverride(URI.create(s3Endpoint))
                        .build();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(s3Bucket)
                        .key(uniqueFileName)
                        .contentType(file.getContentType())
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
                return "https://" + s3Bucket + ".s3." + s3Region + ".amazonaws.com/" + uniqueFileName;
            } catch (Exception e) {
                // Fallback to local storage if S3 fails
            }
        }

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFileName + ". Please try again!", ex);
        }
    }
}
