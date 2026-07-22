package com.example.demo.repository;

import com.example.demo.entity.GithubIntelligence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GithubIntelligenceRepository extends JpaRepository<GithubIntelligence, Long> {
    Optional<GithubIntelligence> findBySubmissionId(Long submissionId);
}
