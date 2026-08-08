package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "skill_cards")
public class SkillCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String primaryRole; // e.g. Frontend, Backend, ML Engineer, UI/UX

    @Column(nullable = false, columnDefinition = "TEXT")
    private String skillsJson; // JSON array of skills e.g. ["React", "Python", "PyTorch"]

    private String timezone;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String githubUrl;
    private String linkedinUrl;
    private Boolean lookingForTeam;

    private LocalDateTime createdAt;

    public SkillCard() {
        this.createdAt = LocalDateTime.now();
        this.lookingForTeam = true;
        this.timezone = "UTC";
    }

    public SkillCard(Long userId, String fullName, String email, String primaryRole, String skillsJson) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.primaryRole = primaryRole;
        this.skillsJson = skillsJson;
        this.createdAt = LocalDateTime.now();
        this.lookingForTeam = true;
        this.timezone = "UTC";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPrimaryRole() { return primaryRole; }
    public void setPrimaryRole(String primaryRole) { this.primaryRole = primaryRole; }

    public String getSkillsJson() { return skillsJson; }
    public void setSkillsJson(String skillsJson) { this.skillsJson = skillsJson; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

    public Boolean getLookingForTeam() { return lookingForTeam; }
    public void setLookingForTeam(Boolean lookingForTeam) { this.lookingForTeam = lookingForTeam; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
