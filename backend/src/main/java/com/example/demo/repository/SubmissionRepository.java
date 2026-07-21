package com.example.demo.repository;

import com.example.demo.entity.Submission;
import com.example.demo.enums.SubmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByTeamNameIgnoreCase(String teamName);

    Page<Submission> findByTeamNameContainingIgnoreCase(String teamName, Pageable pageable);
    List<Submission> findByTeamNameContainingIgnoreCase(String teamName);

    long countByStatus(SubmissionStatus status);
}
