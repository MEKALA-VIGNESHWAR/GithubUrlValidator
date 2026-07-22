package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SubmissionRequestDTO {

    @NotBlank(message = "Team name is required")
    @Size(min = 3, max = 50, message = "Team name must be between 3 and 50 characters")
    private String teamName;

    @NotBlank(message = "Project title is required")
    @Size(min = 3, max = 100, message = "Project title must be between 3 and 100 characters")
    private String projectTitle;

    @NotBlank(message = "GitHub repository URL is required")
    @Pattern(regexp = "^https?://(www\\.)?github\\.com/[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+/?$",
            message = "Must be a valid GitHub repository URL (e.g. https://github.com/username/repository)")
    private String githubRepoUrl;

    private String leaderName;

    private String email;

    private String college;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private String techStack;
    private String demoVideoUrl;
    private String pptUrl;
    private String pdfUrl;

    private String phoneNumber;
    private String members;

    @Size(max = 2000, message = "Problem statement must not exceed 2000 characters")
    private String problemStatement;

    private String category;
    private String projectImageUrl;
    private String difficulty;
    private Integer completionRate;
    private String judgeComment;
    private String languagesJson;

    public SubmissionRequestDTO() {}

    public SubmissionRequestDTO(String teamName, String projectTitle, String githubRepoUrl, String leaderName, String email, String college) {
        this.teamName = teamName;
        this.projectTitle = projectTitle;
        this.githubRepoUrl = githubRepoUrl;
        this.leaderName = leaderName;
        this.email = email;
        this.college = college;
    }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getGithubRepoUrl() { return githubRepoUrl; }
    public void setGithubRepoUrl(String githubRepoUrl) { this.githubRepoUrl = githubRepoUrl; }

    public String getLeaderName() { return leaderName; }
    public void setLeaderName(String leaderName) { this.leaderName = leaderName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }

    public String getDemoVideoUrl() { return demoVideoUrl; }
    public void setDemoVideoUrl(String demoVideoUrl) { this.demoVideoUrl = demoVideoUrl; }

    public String getPptUrl() { return pptUrl; }
    public void setPptUrl(String pptUrl) { this.pptUrl = pptUrl; }

    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }

    public String getProblemStatement() { return problemStatement; }
    public void setProblemStatement(String problemStatement) { this.problemStatement = problemStatement; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getProjectImageUrl() { return projectImageUrl; }
    public void setProjectImageUrl(String projectImageUrl) { this.projectImageUrl = projectImageUrl; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getCompletionRate() { return completionRate; }
    public void setCompletionRate(Integer completionRate) { this.completionRate = completionRate; }

    public String getJudgeComment() { return judgeComment; }
    public void setJudgeComment(String judgeComment) { this.judgeComment = judgeComment; }

    public String getLanguagesJson() { return languagesJson; }
    public void setLanguagesJson(String languagesJson) { this.languagesJson = languagesJson; }
}
