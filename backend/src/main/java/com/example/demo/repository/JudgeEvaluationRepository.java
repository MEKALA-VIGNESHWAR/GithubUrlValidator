package com.example.demo.repository;

import com.example.demo.entity.JudgeEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JudgeEvaluationRepository extends JpaRepository<JudgeEvaluation, Long> {
    List<JudgeEvaluation> findBySubmissionId(Long submissionId);
    List<JudgeEvaluation> findByJudgeId(Long judgeId);
}
