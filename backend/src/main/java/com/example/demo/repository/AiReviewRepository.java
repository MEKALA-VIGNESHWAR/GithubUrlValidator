package com.example.demo.repository;

import com.example.demo.entity.AiReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiReviewRepository extends JpaRepository<AiReview, Long> {
    Optional<AiReview> findBySubmissionId(Long submissionId);
}
