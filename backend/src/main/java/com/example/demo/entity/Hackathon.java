package com.example.demo.entity;

import com.example.demo.enums.HackathonStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hackathons")
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long organizationId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String eventType; // ONLINE, OFFLINE, HYBRID

    private String format;

    @Enumerated(EnumType.STRING)
    private HackathonStatus status = HackathonStatus.UPCOMING;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String rules;

    @Column(length = 2000)
    private String prizes;

    private String organizer;
    private String location;
    private Integer capacity = 500;
    private Integer registeredCount = 0;
    private String prizePool = "$50,000";
    private String difficulty = "Intermediate";
    private String track = "AI & Web3";
    private boolean archived = false;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime submissionDeadline;
    private Boolean isPublished;
    private LocalDateTime createdAt;

    public Hackathon() {
        this.eventType = "ONLINE";
        this.isPublished = true;
        this.createdAt = LocalDateTime.now();
    }

    public Hackathon(Long organizationId, String title, String slug, String eventType, String description, LocalDateTime submissionDeadline) {
        this.organizationId = organizationId;
        this.title = title;
        this.slug = slug != null ? slug.toLowerCase().trim() : "";
        this.eventType = eventType != null ? eventType.toUpperCase() : "ONLINE";
        this.description = description;
        this.submissionDeadline = submissionDeadline;
        this.isPublished = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public HackathonStatus getStatus() { return status; }
    public void setStatus(HackathonStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public String getPrizes() { return prizes; }
    public void setPrizes(String prizes) { this.prizes = prizes; }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getRegisteredCount() { return registeredCount; }
    public void setRegisteredCount(Integer registeredCount) { this.registeredCount = registeredCount; }

    public String getPrizePool() { return prizePool; }
    public void setPrizePool(String prizePool) { this.prizePool = prizePool; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTrack() { return track; }
    public void setTrack(String track) { this.track = track; }

    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public LocalDateTime getSubmissionDeadline() { return submissionDeadline; }
    public void setSubmissionDeadline(LocalDateTime submissionDeadline) { this.submissionDeadline = submissionDeadline; }

    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
