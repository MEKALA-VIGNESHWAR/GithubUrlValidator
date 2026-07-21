package com.example.demo.mapper;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.entity.Submission;

public class SubmissionMapper {

    public static Submission toEntity(SubmissionRequestDTO dto) {
        if (dto == null) return null;

        Submission entity = new Submission();
        entity.setTeamName(dto.getTeamName());
        entity.setProjectTitle(dto.getProjectTitle());
        entity.setGithubRepoUrl(dto.getGithubRepoUrl());
        entity.setLeaderName(dto.getLeaderName());
        entity.setEmail(dto.getEmail());
        entity.setCollege(dto.getCollege());
        entity.setDescription(dto.getDescription());
        entity.setTechStack(dto.getTechStack());
        entity.setDemoVideoUrl(dto.getDemoVideoUrl());
        entity.setPptUrl(dto.getPptUrl());
        entity.setPdfUrl(dto.getPdfUrl());
        return entity;
    }

    public static SubmissionResponseDTO toDTO(Submission entity) {
        if (entity == null) return null;

        SubmissionResponseDTO dto = new SubmissionResponseDTO();
        dto.setId(entity.getId());
        dto.setTeamName(entity.getTeamName());
        dto.setProjectTitle(entity.getProjectTitle());
        dto.setGithubRepoUrl(entity.getGithubRepoUrl());
        dto.setLeaderName(entity.getLeaderName());
        dto.setEmail(entity.getEmail());
        dto.setCollege(entity.getCollege());
        dto.setDescription(entity.getDescription());
        dto.setTechStack(entity.getTechStack());
        dto.setDemoVideoUrl(entity.getDemoVideoUrl());
        dto.setPptUrl(entity.getPptUrl());
        dto.setPdfUrl(entity.getPdfUrl());
        dto.setSubmittedAt(entity.getSubmittedAt());
        dto.setStatus(entity.getStatus());
        dto.setRepoOwner(entity.getRepoOwner());
        dto.setRepoName(entity.getRepoName());
        dto.setStars(entity.getStars());
        dto.setForks(entity.getForks());
        dto.setOpenIssues(entity.getOpenIssues());
        dto.setLastCommitDate(entity.getLastCommitDate());
        return dto;
    }

    public static void updateEntityFromDTO(SubmissionRequestDTO dto, Submission entity) {
        if (dto == null || entity == null) return;

        if (dto.getTeamName() != null) entity.setTeamName(dto.getTeamName().trim());
        if (dto.getProjectTitle() != null) entity.setProjectTitle(dto.getProjectTitle().trim());
        if (dto.getGithubRepoUrl() != null) entity.setGithubRepoUrl(dto.getGithubRepoUrl().trim());
        if (dto.getLeaderName() != null) entity.setLeaderName(dto.getLeaderName().trim());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail().trim());
        if (dto.getCollege() != null) entity.setCollege(dto.getCollege().trim());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription().trim());
        if (dto.getTechStack() != null) entity.setTechStack(dto.getTechStack().trim());
        if (dto.getDemoVideoUrl() != null) entity.setDemoVideoUrl(dto.getDemoVideoUrl().trim());
        if (dto.getPptUrl() != null) entity.setPptUrl(dto.getPptUrl().trim());
        if (dto.getPdfUrl() != null) entity.setPdfUrl(dto.getPdfUrl().trim());
    }
}
