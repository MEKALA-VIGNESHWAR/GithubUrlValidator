package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class PresignedUrlService {

    private static final Logger log = LoggerFactory.getLogger(PresignedUrlService.class);

    @Value("${app.s3.bucket-name:hackforge-storage}")
    private String s3BucketName;

    @Value("${app.s3.region:us-east-1}")
    private String awsRegion;

    public Map<String, Object> generatePresignedUploadUrl(String originalFileName, String contentType) {
        String safeFileName = UUID.randomUUID() + "-" + originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String s3Key = "uploads/" + safeFileName;

        // AWS S3 Presigned PUT URL format
        String presignedUrl = String.format(
            "https://%s.s3.%s.amazonaws.com/%s?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=DEMO_CREDENTIAL&X-Amz-Date=20260808T120000Z&X-Amz-Expires=900&X-Amz-SignedHeaders=host&X-Amz-Signature=DEMO_SIGNATURE",
            s3BucketName, awsRegion, s3Key
        );

        String finalPublicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", s3BucketName, awsRegion, s3Key);

        log.info("Generated S3 Presigned Upload URL for file {}: key={}", originalFileName, s3Key);

        return Map.of(
            "presignedUrl", presignedUrl,
            "s3Key", s3Key,
            "fileUrl", finalPublicUrl,
            "expiresInSeconds", 900,
            "contentType", contentType != null ? contentType : "application/octet-stream"
        );
    }

    public Map<String, Object> confirmUpload(String s3Key, String fileName) {
        log.info("Direct S3 upload confirmed for key {}", s3Key);
        String finalUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", s3BucketName, awsRegion, s3Key);
        return Map.of(
            "status", "CONFIRMED",
            "s3Key", s3Key,
            "fileUrl", finalUrl,
            "confirmedAt", Instant.now().toString()
        );
    }
}
