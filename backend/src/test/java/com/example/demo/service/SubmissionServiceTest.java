package com.example.demo.service;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.GithubRepoInfoDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.entity.Submission;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.InvalidSubmissionException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.impl.SubmissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository repository;

    @Mock
    private GithubApiService githubApiService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SubmissionServiceImpl submissionService;

    private SubmissionRequestDTO validRequest;
    private Submission validSubmission;

    @BeforeEach
    void setUp() {
        validRequest = new SubmissionRequestDTO("AlphaTeam", "Cool AI Project", "https://github.com/alphateam/cool-ai", "John Doe", "john@example.com", "MIT");
        validSubmission = new Submission("AlphaTeam", "Cool AI Project", "https://github.com/alphateam/cool-ai");
        validSubmission.setId(1L);
        validSubmission.setLeaderName("John Doe");
        validSubmission.setEmail("john@example.com");
        validSubmission.setCollege("MIT");
    }

    @Test
    void submitProject_Success() {
        when(repository.existsByTeamNameIgnoreCase("AlphaTeam")).thenReturn(false);
        when(githubApiService.fetchRepositoryDetails("https://github.com/alphateam/cool-ai"))
                .thenReturn(new GithubRepoInfoDTO("cool-ai", "alphateam", 10, 2, 0, "2026-07-21"));
        when(repository.save(any(Submission.class))).thenReturn(validSubmission);

        SubmissionResponseDTO created = submissionService.submitProject(validRequest);

        assertNotNull(created.getId());
        assertEquals("AlphaTeam", created.getTeamName());
        assertEquals("Cool AI Project", created.getProjectTitle());
        assertEquals("https://github.com/alphateam/cool-ai", created.getGithubRepoUrl());
        verify(repository, times(1)).save(any(Submission.class));
    }

    @Test
    void submitProject_DuplicateTeamName_ThrowsConflictException() {
        when(repository.existsByTeamNameIgnoreCase("AlphaTeam")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                submissionService.submitProject(validRequest)
        );

        assertTrue(ex.getMessage().contains("already submitted"));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllSubmissionsList_ReturnsList() {
        when(repository.findAll()).thenReturn(Arrays.asList(validSubmission));

        List<SubmissionResponseDTO> result = submissionService.getAllSubmissionsList();

        assertEquals(1, result.size());
        assertEquals("AlphaTeam", result.get(0).getTeamName());
    }

    @Test
    void getSubmissionById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(validSubmission));

        SubmissionResponseDTO result = submissionService.getSubmissionById(1L);

        assertEquals("AlphaTeam", result.getTeamName());
    }

    @Test
    void getSubmissionById_NotFound_ThrowsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                submissionService.getSubmissionById(99L)
        );
    }

    @Test
    void deleteSubmission_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(validSubmission));

        submissionService.deleteSubmission(1L);

        verify(repository, times(1)).delete(validSubmission);
    }

    @Test
    void deleteSubmission_NotFound_ThrowsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                submissionService.deleteSubmission(99L)
        );
    }
}
