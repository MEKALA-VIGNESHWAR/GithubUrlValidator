package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "plagiarism_reports")
public class PlagiarismReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long submissionId;

    @Column(nullable = false)
    private Double similarityScore;

    private Long matchedSubmissionId;

    @Column(length = 1000)
    private String matchedSourceUrl;

    @Column(columnDefinition = "TEXT")
    private String flaggedSnippetsJson;

    private String status;

    private LocalDateTime scannedAt;

    public PlagiarismReport() {
        this.scannedAt = LocalDateTime.now();
        this.status = "COMPLETED";
    }

    public PlagiarismReport(Long submissionId, Double similarityScore, Long matchedSubmissionId, String matchedSourceUrl, String flaggedSnippetsJson) {
        this.submissionId = submissionId;
        this.similarityScore = similarityScore;
        this.matchedSubmissionId = matchedSubmissionId;
        this.matchedSourceUrl = matchedSourceUrl;
        this.flaggedSnippetsJson = flaggedSnippetsJson;
        this.status = "COMPLETED";
        this.scannedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Double getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(Double similarityScore) { this.similarityScore = similarityScore; }

    public Long getMatchedSubmissionId() { return matchedSubmissionId; }
    public void setMatchedSubmissionId(Long matchedSubmissionId) { this.matchedSubmissionId = matchedSubmissionId; }

    public String getMatchedSourceUrl() { return matchedSourceUrl; }
    public void setMatchedSourceUrl(String matchedSourceUrl) { this.matchedSourceUrl = matchedSourceUrl; }

    public String getFlaggedSnippetsJson() { return flaggedSnippetsJson; }
    public void setFlaggedSnippetsJson(String flaggedSnippetsJson) { this.flaggedSnippetsJson = flaggedSnippetsJson; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }
}
