package com.example.demo.service;

import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    private Submission sampleSubmission;

    @BeforeEach
    void setUp() {
        sampleSubmission = new Submission("Team Alpha", "AI Fraud Detector", "https://github.com/owner/repo");
        sampleSubmission.setId(1L);
    }

    @Test
    void createSubmission_ShouldReturnSavedSubmission() {
        when(submissionRepository.save(any(Submission.class))).thenReturn(sampleSubmission);

        Submission response = submissionRepository.save(sampleSubmission);

        assertNotNull(response);
        assertEquals("Team Alpha", response.getTeamName());
        assertEquals("AI Fraud Detector", response.getProjectTitle());
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void updateStatus_ShouldChangeStatusAndReturnUpdatedSubmission() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.of(sampleSubmission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(sampleSubmission);

        Submission sub = submissionRepository.findById(1L).orElse(null);
        assertNotNull(sub);
        sub.setStatus(SubmissionStatus.APPROVED);
        Submission response = submissionRepository.save(sub);

        assertNotNull(response);
        assertEquals(SubmissionStatus.APPROVED, response.getStatus());
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }
}
