package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "repository_details")
public class RepositoryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long submissionId;

    private String owner;

    private Integer stars;

    private Integer forks;

    private Integer openIssues;

    @Column(length = 1000)
    private String languages;

    private String lastCommit;

    private Boolean hasReadme;

    private Boolean isPublic;

    public RepositoryDetails() {}

    public RepositoryDetails(Long submissionId, String owner, Integer stars, Integer forks, Integer openIssues, String languages, String lastCommit) {
        this.submissionId = submissionId;
        this.owner = owner;
        this.stars = stars;
        this.forks = forks;
        this.openIssues = openIssues;
        this.languages = languages;
        this.lastCommit = lastCommit;
        this.hasReadme = true;
        this.isPublic = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }

    public Integer getForks() { return forks; }
    public void setForks(Integer forks) { this.forks = forks; }

    public Integer getOpenIssues() { return openIssues; }
    public void setOpenIssues(Integer openIssues) { this.openIssues = openIssues; }

    public String getLanguages() { return languages; }
    public void setLanguages(String languages) { this.languages = languages; }

    public String getLastCommit() { return lastCommit; }
    public void setLastCommit(String lastCommit) { this.lastCommit = lastCommit; }

    public Boolean getHasReadme() { return hasReadme; }
    public void setHasReadme(Boolean hasReadme) { this.hasReadme = hasReadme; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}
