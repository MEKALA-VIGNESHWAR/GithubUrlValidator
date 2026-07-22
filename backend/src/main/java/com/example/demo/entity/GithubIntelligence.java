package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "github_intelligence")
public class GithubIntelligence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long submissionId;

    private Integer commitCount;
    private Integer openIssues;
    private Integer pullRequests;

    @Column(length = 2000)
    private String languageBreakdownJson;

    private Double teamBalanceScore;
    private Boolean codeFreezeValid;

    private LocalDateTime analyzedAt;

    public GithubIntelligence() {
        this.analyzedAt = LocalDateTime.now();
    }

    public GithubIntelligence(Long submissionId, Integer commitCount, Integer openIssues, Integer pullRequests, String languageBreakdownJson, Double teamBalanceScore, Boolean codeFreezeValid) {
        this.submissionId = submissionId;
        this.commitCount = commitCount;
        this.openIssues = openIssues;
        this.pullRequests = pullRequests;
        this.languageBreakdownJson = languageBreakdownJson;
        this.teamBalanceScore = teamBalanceScore;
        this.codeFreezeValid = codeFreezeValid;
        this.analyzedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Integer getCommitCount() { return commitCount; }
    public void setCommitCount(Integer commitCount) { this.commitCount = commitCount; }

    public Integer getOpenIssues() { return openIssues; }
    public void setOpenIssues(Integer openIssues) { this.openIssues = openIssues; }

    public Integer getPullRequests() { return pullRequests; }
    public void setPullRequests(Integer pullRequests) { this.pullRequests = pullRequests; }

    public String getLanguageBreakdownJson() { return languageBreakdownJson; }
    public void setLanguageBreakdownJson(String languageBreakdownJson) { this.languageBreakdownJson = languageBreakdownJson; }

    public Double getTeamBalanceScore() { return teamBalanceScore; }
    public void setTeamBalanceScore(Double teamBalanceScore) { this.teamBalanceScore = teamBalanceScore; }

    public Boolean getCodeFreezeValid() { return codeFreezeValid; }
    public void setCodeFreezeValid(Boolean codeFreezeValid) { this.codeFreezeValid = codeFreezeValid; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}
