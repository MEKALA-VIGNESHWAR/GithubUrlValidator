package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_reviews")
public class AiReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long submissionId;

    private Double innovationScore;
    private Double technicalScore;
    private Double documentationScore;
    private Double riskScore;

    @Column(length = 2000)
    private String aiSummary;

    @Column(length = 1000)
    private String sponsorSpotlight;

    private LocalDateTime createdAt;

    public AiReview() {
        this.createdAt = LocalDateTime.now();
    }

    public AiReview(Long submissionId, Double innovationScore, Double technicalScore, Double documentationScore, Double riskScore, String aiSummary, String sponsorSpotlight) {
        this.submissionId = submissionId;
        this.innovationScore = innovationScore;
        this.technicalScore = technicalScore;
        this.documentationScore = documentationScore;
        this.riskScore = riskScore;
        this.aiSummary = aiSummary;
        this.sponsorSpotlight = sponsorSpotlight;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Double getInnovationScore() { return innovationScore; }
    public void setInnovationScore(Double innovationScore) { this.innovationScore = innovationScore; }

    public Double getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Double technicalScore) { this.technicalScore = technicalScore; }

    public Double getDocumentationScore() { return documentationScore; }
    public void setDocumentationScore(Double documentationScore) { this.documentationScore = documentationScore; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public String getSponsorSpotlight() { return sponsorSpotlight; }
    public void setSponsorSpotlight(String sponsorSpotlight) { this.sponsorSpotlight = sponsorSpotlight; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
