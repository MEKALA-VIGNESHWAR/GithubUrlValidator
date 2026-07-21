package com.example.demo.service.impl;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.GithubRepoInfoDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.InvalidSubmissionException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.SubmissionClosedException;
import com.example.demo.mapper.SubmissionMapper;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.GithubApiService;
import com.example.demo.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository repository;
    private final GithubApiService githubApiService;
    private final EmailService emailService;

    private static final LocalDateTime DEADLINE = LocalDateTime.of(2030, 3, 1, 23, 59);

    public SubmissionServiceImpl(SubmissionRepository repository,
                                 GithubApiService githubApiService,
                                 @Autowired(required = false) EmailService emailService) {
        this.repository = repository;
        this.githubApiService = githubApiService;
        this.emailService = emailService;
    }

    @Override
    public SubmissionResponseDTO submitProject(SubmissionRequestDTO requestDTO) {
        if (LocalDateTime.now().isAfter(DEADLINE)) {
            throw new SubmissionClosedException("The hackathon deadline has passed. Submissions are closed.");
        }

        if (repository.existsByTeamNameIgnoreCase(requestDTO.getTeamName().trim())) {
            throw new ConflictException("Team '" + requestDTO.getTeamName().trim() + "' has already submitted a project.");
        }

        // Validate GitHub API and fetch stats
        GithubRepoInfoDTO repoInfo = githubApiService.fetchRepositoryDetails(requestDTO.getGithubRepoUrl());

        Submission submission = SubmissionMapper.toEntity(requestDTO);
        submission.setTeamName(requestDTO.getTeamName().trim());
        submission.setProjectTitle(requestDTO.getProjectTitle().trim());
        submission.setGithubRepoUrl(requestDTO.getGithubRepoUrl().trim());
        submission.setRepoOwner(repoInfo.getOwner());
        submission.setRepoName(repoInfo.getName());
        submission.setStars(repoInfo.getStars());
        submission.setForks(repoInfo.getForks());
        submission.setOpenIssues(repoInfo.getOpenIssues());
        submission.setLastCommitDate(repoInfo.getLastCommitDate());

        Submission saved = repository.save(submission);

        if (emailService != null && saved.getEmail() != null) {
            emailService.sendSubmissionConfirmationEmail(saved.getEmail(), saved.getTeamName(), saved.getProjectTitle());
        }

        return SubmissionMapper.toDTO(saved);
    }

    @Override
    public Page<SubmissionResponseDTO> getAllSubmissions(Pageable pageable) {
        return repository.findAll(pageable).map(SubmissionMapper::toDTO);
    }

    @Override
    public List<SubmissionResponseDTO> getAllSubmissionsList() {
        return repository.findAll().stream()
                .map(SubmissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubmissionResponseDTO getSubmissionById(Long id) {
        Submission submission = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission with ID " + id + " was not found."));
        return SubmissionMapper.toDTO(submission);
    }

    @Override
    public SubmissionResponseDTO updateSubmission(Long id, SubmissionRequestDTO requestDTO) {
        Submission submission = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission with ID " + id + " was not found."));

        if (!submission.getTeamName().equalsIgnoreCase(requestDTO.getTeamName().trim()) &&
                repository.existsByTeamNameIgnoreCase(requestDTO.getTeamName().trim())) {
            throw new ConflictException("Team '" + requestDTO.getTeamName().trim() + "' has already submitted a project.");
        }

        SubmissionMapper.updateEntityFromDTO(requestDTO, submission);

        if (requestDTO.getGithubRepoUrl() != null) {
            GithubRepoInfoDTO repoInfo = githubApiService.fetchRepositoryDetails(requestDTO.getGithubRepoUrl());
            submission.setRepoOwner(repoInfo.getOwner());
            submission.setRepoName(repoInfo.getName());
            submission.setStars(repoInfo.getStars());
            submission.setForks(repoInfo.getForks());
            submission.setOpenIssues(repoInfo.getOpenIssues());
            submission.setLastCommitDate(repoInfo.getLastCommitDate());
        }

        Submission updated = repository.save(submission);
        return SubmissionMapper.toDTO(updated);
    }

    @Override
    public SubmissionResponseDTO updateSubmissionStatus(Long id, SubmissionStatus status) {
        Submission submission = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission with ID " + id + " was not found."));

        submission.setStatus(status);
        Submission updated = repository.save(submission);

        if (emailService != null && updated.getEmail() != null) {
            emailService.sendSubmissionStatusEmail(updated.getEmail(), updated.getTeamName(), updated.getProjectTitle(), status);
        }

        return SubmissionMapper.toDTO(updated);
    }

    @Override
    public void deleteSubmission(Long id) {
        Submission submission = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission with ID " + id + " was not found."));
        repository.delete(submission);
    }

    @Override
    public List<SubmissionResponseDTO> searchSubmissionsByTeam(String teamName) {
        return repository.findByTeamNameContainingIgnoreCase(teamName).stream()
                .map(SubmissionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
