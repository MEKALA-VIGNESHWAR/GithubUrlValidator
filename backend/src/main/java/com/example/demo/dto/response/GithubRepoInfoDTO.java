package com.example.demo.dto.response;

public class GithubRepoInfoDTO {

    private String name;
    private String owner;
    private Integer stars;
    private Integer forks;
    private Integer openIssues;
    private String lastCommitDate;

    public GithubRepoInfoDTO() {}

    public GithubRepoInfoDTO(String name, String owner, Integer stars, Integer forks, Integer openIssues, String lastCommitDate) {
        this.name = name;
        this.owner = owner;
        this.stars = stars;
        this.forks = forks;
        this.openIssues = openIssues;
        this.lastCommitDate = lastCommitDate;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public Integer getStars() { return stars; }
    public void setStars(Integer stars) { this.stars = stars; }

    public Integer getForks() { return forks; }
    public void setForks(Integer forks) { this.forks = forks; }

    public Integer getOpenIssues() { return openIssues; }
    public void setOpenIssues(Integer openIssues) { this.openIssues = openIssues; }

    public String getLastCommitDate() { return lastCommitDate; }
    public void setLastCommitDate(String lastCommitDate) { this.lastCommitDate = lastCommitDate; }
}
