package com.example.demo.repository;

import com.example.demo.entity.JudgeExpertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JudgeExpertiseRepository extends JpaRepository<JudgeExpertise, Long> {
    Optional<JudgeExpertise> findByUserId(Long userId);
}
