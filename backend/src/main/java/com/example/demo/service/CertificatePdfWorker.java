package com.example.demo.service;

import com.example.demo.entity.Certificate;
import com.example.demo.repository.CertificateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CertificatePdfWorker {

    private static final Logger log = LoggerFactory.getLogger(CertificatePdfWorker.class);

    private final CertificateRepository certificateRepository;

    public CertificatePdfWorker(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @RabbitListener(queues = "${app.rabbitmq.certificate-queue:certificateGenQueue}", autoStartup = "false")
    public void processCertificateGenerationEvent(String verificationToken) {
        log.info("Processing background PDF certificate generation for token: {}", verificationToken);

        try {
            Certificate cert = certificateRepository.findByVerificationHash(verificationToken).orElse(null);
            if (cert == null) {
                log.warn("Certificate not found for verification hash: {}", verificationToken);
                return;
            }

            // Simulate PDF rendering & AWS S3 upload
            String s3PdfUrl = "https://hackforge-storage.s3.amazonaws.com/certificates/" + verificationToken + ".pdf";
            String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://hackforge-nlb2.onrender.com/verify/certificate/" + verificationToken;

            cert.setPdfUrl(s3PdfUrl);
            cert.setQrCodeUrl(qrUrl);
            certificateRepository.save(cert);

            log.info("Successfully generated PDF certificate and stored in S3: {}", s3PdfUrl);
        } catch (Exception e) {
            log.error("Failed to generate PDF certificate for token {}", verificationToken, e);
        }
    }
}
