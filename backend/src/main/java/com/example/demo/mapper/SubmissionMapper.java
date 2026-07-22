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
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setMembers(dto.getMembers());
        entity.setProblemStatement(dto.getProblemStatement());
        entity.setCategory(dto.getCategory());
        entity.setProjectImageUrl(dto.getProjectImageUrl());
        entity.setDifficulty(dto.getDifficulty());
        entity.setCompletionRate(dto.getCompletionRate());
        entity.setJudgeComment(dto.getJudgeComment());
        entity.setLanguagesJson(dto.getLanguagesJson());
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
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setMembers(entity.getMembers());
        dto.setProblemStatement(entity.getProblemStatement());
        dto.setCategory(entity.getCategory());
        dto.setProjectImageUrl(entity.getProjectImageUrl());
        dto.setDifficulty(entity.getDifficulty());
        dto.setCompletionRate(entity.getCompletionRate());
        dto.setJudgeComment(entity.getJudgeComment());
        dto.setLanguagesJson(entity.getLanguagesJson());
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
        if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber().trim());
        if (dto.getMembers() != null) entity.setMembers(dto.getMembers().trim());
        if (dto.getProblemStatement() != null) entity.setProblemStatement(dto.getProblemStatement().trim());
        if (dto.getCategory() != null) entity.setCategory(dto.getCategory().trim());
        if (dto.getProjectImageUrl() != null) entity.setProjectImageUrl(dto.getProjectImageUrl().trim());
        if (dto.getDifficulty() != null) entity.setDifficulty(dto.getDifficulty().trim());
        if (dto.getCompletionRate() != null) entity.setCompletionRate(dto.getCompletionRate());
        if (dto.getJudgeComment() != null) entity.setJudgeComment(dto.getJudgeComment().trim());
        if (dto.getLanguagesJson() != null) entity.setLanguagesJson(dto.getLanguagesJson().trim());
    }
}
