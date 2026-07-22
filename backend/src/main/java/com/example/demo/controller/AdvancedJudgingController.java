package com.example.demo.controller;

import com.example.demo.entity.Certificate;
import com.example.demo.entity.JudgeEvaluation;
import com.example.demo.entity.QrCheckin;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.JudgeEvaluationRepository;
import com.example.demo.repository.QrCheckinRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AdvancedJudgingController {

    private final JudgeEvaluationRepository judgeEvaluationRepository;
    private final QrCheckinRepository qrCheckinRepository;
    private final CertificateRepository certificateRepository;

    public AdvancedJudgingController(JudgeEvaluationRepository judgeEvaluationRepository,
                                     QrCheckinRepository qrCheckinRepository,
                                     CertificateRepository certificateRepository) {
        this.judgeEvaluationRepository = judgeEvaluationRepository;
        this.qrCheckinRepository = qrCheckinRepository;
        this.certificateRepository = certificateRepository;
    }

    // --- Judging Endpoints ---
    @PostMapping("/judging/evaluations")
    public ResponseEntity<?> submitEvaluation(@RequestBody JudgeEvaluation evaluation) {
        if (evaluation.getSubmissionId() == null || evaluation.getJudgeId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Submission ID and Judge ID are required"));
        }

        // Anomaly flag check (e.g. score > 95 or score < 20)
        if (evaluation.getScore() != null && (evaluation.getScore() > 95.0 || evaluation.getScore() < 20.0)) {
            evaluation.setAnomalyFlagged(true);
        }

        JudgeEvaluation saved = judgeEvaluationRepository.save(evaluation);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/judging/evaluations/submission/{submissionId}")
    public ResponseEntity<List<JudgeEvaluation>> getEvaluationsForSubmission(@PathVariable Long submissionId) {
        return ResponseEntity.ok(judgeEvaluationRepository.findBySubmissionId(submissionId));
    }

    // --- QR Check-in Endpoints ---
    @PostMapping("/qr/checkin")
    public ResponseEntity<?> recordQrCheckin(@RequestBody QrCheckin checkin) {
        if (checkin.getUserId() == null || checkin.getHackathonId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User ID and Hackathon ID are required"));
        }

        QrCheckin saved = qrCheckinRepository.save(checkin);
        return ResponseEntity.ok(saved);
    }

    // --- Certificate Endpoints ---
    @PostMapping("/certificates/generate")
    public ResponseEntity<?> generateCertificate(@RequestBody Certificate cert) {
        if (cert.getUserId() == null || cert.getRecipientName() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User ID and Recipient Name are required"));
        }

        String hash = "HF-CERT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        cert.setVerificationHash(hash);

        Certificate saved = certificateRepository.save(cert);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/certificates/verify/{hash}")
    public ResponseEntity<?> verifyCertificate(@PathVariable String hash) {
        Certificate cert = certificateRepository.findByVerificationHash(hash.toUpperCase()).orElse(null);
        if (cert == null) {
            return ResponseEntity.status(404).body(Map.of("valid", false, "message", "Invalid or unverified certificate hash"));
        }

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "recipientName", cert.getRecipientName(),
                "certificateType", cert.getCertificateType(),
                "issuedAt", cert.getIssuedAt(),
                "verificationHash", cert.getVerificationHash()
        ));
    }
}
