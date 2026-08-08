package com.example.demo.controller;

import com.example.demo.entity.Certificate;
import com.example.demo.repository.CertificateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/verify/certificate")
public class CertificateVerificationController {

    private final CertificateRepository certificateRepository;

    public CertificateVerificationController(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @GetMapping("/{token}")
    public ResponseEntity<?> verifyOpenBadgeCertificate(@PathVariable String token) {
        Certificate cert = certificateRepository.findByVerificationHash(token.toUpperCase()).orElse(null);

        if (cert == null) {
            return ResponseEntity.status(404).body(Map.of(
                "valid", false,
                "status", "INVALID_OR_NOT_FOUND",
                "message", "The certificate token is invalid, forged, or has been revoked."
            ));
        }

        Map<String, Object> openBadgeMetadata = Map.of(
            "@context", "https://w3id.org/openbadges/v2",
            "type", "Assertion",
            "id", "https://hackforge-nlb2.onrender.com/verify/certificate/" + cert.getVerificationHash(),
            "recipient", Map.of(
                "type", "email",
                "hashed", false,
                "identity", "participant@hackforge.io"
            ),
            "issuedOn", cert.getIssuedAt().toString(),
            "badge", Map.of(
                "id", "https://hackforge.io/badges/" + cert.getCertificateType().toLowerCase(),
                "type", "BadgeClass",
                "name", "HackForge " + cert.getCertificateType() + " Award",
                "description", "Issued for outstanding performance in Enterprise Hackathon competition.",
                "image", "https://hackforge.io/assets/badge-gold.png",
                "issuer", "https://hackforge.io/organization.json"
            ),
            "verification", Map.of(
                "type", "hosted",
                "url", "https://hackforge-nlb2.onrender.com/verify/certificate/" + cert.getVerificationHash()
            )
        );

        return ResponseEntity.ok(Map.of(
            "valid", true,
            "verificationToken", cert.getVerificationHash(),
            "recipientName", cert.getRecipientName(),
            "certificateType", cert.getCertificateType(),
            "issuedAt", cert.getIssuedAt(),
            "pdfUrl", cert.getPdfUrl() != null ? cert.getPdfUrl() : "https://hackforge-storage.s3.amazonaws.com/certificates/" + cert.getVerificationHash() + ".pdf",
            "qrCodeUrl", cert.getQrCodeUrl() != null ? cert.getQrCodeUrl() : "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://hackforge-nlb2.onrender.com/verify/certificate/" + cert.getVerificationHash(),
            "openBadgeAssertion", openBadgeMetadata
        ));
    }
}
