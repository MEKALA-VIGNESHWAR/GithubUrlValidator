package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "judge_expertise")
public class JudgeExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expertiseTagsJson; // e.g. ["AI/ML", "FinTech", "Web3"]

    private Integer maxWorkload;
    private Integer currentWorkload;

    public JudgeExpertise() {
        this.maxWorkload = 10;
        this.currentWorkload = 0;
    }

    public JudgeExpertise(Long userId, String expertiseTagsJson, Integer maxWorkload) {
        this.userId = userId;
        this.expertiseTagsJson = expertiseTagsJson;
        this.maxWorkload = maxWorkload != null ? maxWorkload : 10;
        this.currentWorkload = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getExpertiseTagsJson() { return expertiseTagsJson; }
    public void setExpertiseTagsJson(String expertiseTagsJson) { this.expertiseTagsJson = expertiseTagsJson; }

    public Integer getMaxWorkload() { return maxWorkload; }
    public void setMaxWorkload(Integer maxWorkload) { this.maxWorkload = maxWorkload; }

    public Integer getCurrentWorkload() { return currentWorkload; }
    public void setCurrentWorkload(Integer currentWorkload) { this.currentWorkload = currentWorkload; }
}
