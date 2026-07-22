package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "judge_evaluations")
public class JudgeEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long submissionId;

    @Column(nullable = false)
    private Long judgeId;

    private String roundName;
    private Double score;

    @Column(length = 2000)
    private String rubricBreakdownJson;

    @Column(length = 1000)
    private String comment;

    private Boolean anomalyFlagged;
    private LocalDateTime evaluatedAt;

    public JudgeEvaluation() {
        this.evaluatedAt = LocalDateTime.now();
        this.anomalyFlagged = false;
    }

    public JudgeEvaluation(Long submissionId, Long judgeId, String roundName, Double score, String rubricBreakdownJson, String comment) {
        this.submissionId = submissionId;
        this.judgeId = judgeId;
        this.roundName = roundName != null ? roundName : "Round 1";
        this.score = score;
        this.rubricBreakdownJson = rubricBreakdownJson;
        this.comment = comment;
        this.anomalyFlagged = false;
        this.evaluatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getJudgeId() { return judgeId; }
    public void setJudgeId(Long judgeId) { this.judgeId = judgeId; }

    public String getRoundName() { return roundName; }
    public void setRoundName(String roundName) { this.roundName = roundName; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getRubricBreakdownJson() { return rubricBreakdownJson; }
    public void setRubricBreakdownJson(String rubricBreakdownJson) { this.rubricBreakdownJson = rubricBreakdownJson; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Boolean getAnomalyFlagged() { return anomalyFlagged; }
    public void setAnomalyFlagged(Boolean anomalyFlagged) { this.anomalyFlagged = anomalyFlagged; }

    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
}
