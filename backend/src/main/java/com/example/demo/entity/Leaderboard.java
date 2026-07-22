package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long teamId;

    private Integer rank;

    private Double score;

    private String badge;

    public Leaderboard() {}

    public Leaderboard(Long teamId, Integer rank, Double score, String badge) {
        this.teamId = teamId;
        this.rank = rank;
        this.score = score;
        this.badge = badge;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
}
