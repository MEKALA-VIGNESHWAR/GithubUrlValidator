package com.example.demo.service;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.enums.SubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubmissionService {
    SubmissionResponseDTO submitProject(SubmissionRequestDTO submissionRequestDTO);
    Page<SubmissionResponseDTO> getAllSubmissions(Pageable pageable);
    List<SubmissionResponseDTO> getAllSubmissionsList();
    SubmissionResponseDTO getSubmissionById(Long id);
    SubmissionResponseDTO updateSubmission(Long id, SubmissionRequestDTO requestDTO);
    SubmissionResponseDTO updateSubmissionStatus(Long id, SubmissionStatus status);
    void deleteSubmission(Long id);
    List<SubmissionResponseDTO> searchSubmissionsByTeam(String teamName);
}
