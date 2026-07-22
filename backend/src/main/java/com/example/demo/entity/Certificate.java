package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long submissionId;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String certificateType; // PARTICIPANT, WINNER, JUDGE, MENTOR

    @Column(nullable = false, unique = true)
    private String verificationHash;

    private LocalDateTime issuedAt;

    public Certificate() {
        this.issuedAt = LocalDateTime.now();
    }

    public Certificate(Long userId, Long submissionId, String recipientName, String certificateType, String verificationHash) {
        this.userId = userId;
        this.submissionId = submissionId;
        this.recipientName = recipientName;
        this.certificateType = certificateType != null ? certificateType.toUpperCase() : "PARTICIPANT";
        this.verificationHash = verificationHash;
        this.issuedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getCertificateType() { return certificateType; }
    public void setCertificateType(String certificateType) { this.certificateType = certificateType; }

    public String getVerificationHash() { return verificationHash; }
    public void setVerificationHash(String verificationHash) { this.verificationHash = verificationHash; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
}
