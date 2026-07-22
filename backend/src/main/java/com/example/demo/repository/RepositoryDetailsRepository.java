package com.example.demo.repository;

import com.example.demo.entity.RepositoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RepositoryDetailsRepository extends JpaRepository<RepositoryDetails, Long> {
    Optional<RepositoryDetails> findBySubmissionId(Long submissionId);
}
