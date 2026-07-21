package com.example.demo.dto.response;

import java.util.Map;

public class AnalyticsResponseDTO {

    private long totalProjects;
    private long approved;
    private long pending;
    private long rejected;
    private Map<String, Long> topTechnologies;
    private Map<String, Long> topColleges;
    private Map<String, Long> submissionTrends;

    public AnalyticsResponseDTO() {}

    public AnalyticsResponseDTO(long totalProjects, long approved, long pending, long rejected) {
        this.totalProjects = totalProjects;
        this.approved = approved;
        this.pending = pending;
        this.rejected = rejected;
    }

    public long getTotalProjects() { return totalProjects; }
    public void setTotalProjects(long totalProjects) { this.totalProjects = totalProjects; }

    public long getApproved() { return approved; }
    public void setApproved(long approved) { this.approved = approved; }

    public long getPending() { return pending; }
    public void setPending(long pending) { this.pending = pending; }

    public long getRejected() { return rejected; }
    public void setRejected(long rejected) { this.rejected = rejected; }

    public Map<String, Long> getTopTechnologies() { return topTechnologies; }
    public void setTopTechnologies(Map<String, Long> topTechnologies) { this.topTechnologies = topTechnologies; }

    public Map<String, Long> getTopColleges() { return topColleges; }
    public void setTopColleges(Map<String, Long> topColleges) { this.topColleges = topColleges; }

    public Map<String, Long> getSubmissionTrends() { return submissionTrends; }
    public void setSubmissionTrends(Map<String, Long> submissionTrends) { this.submissionTrends = submissionTrends; }
}
