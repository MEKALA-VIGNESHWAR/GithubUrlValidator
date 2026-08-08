package com.example.demo.service;

import com.example.demo.entity.JudgeEvaluation;
import com.example.demo.entity.Submission;
import com.example.demo.repository.JudgeEvaluationRepository;
import com.example.demo.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreCalibrationService {

    private static final Logger log = LoggerFactory.getLogger(ScoreCalibrationService.class);

    private final JudgeEvaluationRepository judgeEvaluationRepository;
    private final SubmissionRepository submissionRepository;

    public ScoreCalibrationService(JudgeEvaluationRepository judgeEvaluationRepository,
                                   SubmissionRepository submissionRepository) {
        this.judgeEvaluationRepository = judgeEvaluationRepository;
        this.submissionRepository = submissionRepository;
    }

    public Map<String, Object> calibrateScores(Long hackathonId) {
        List<JudgeEvaluation> evaluations = judgeEvaluationRepository.findAll();

        if (evaluations.isEmpty()) {
            return Map.of("message", "No judge evaluations found to calibrate.", "calibratedCount", 0);
        }

        // Group evaluations by judgeId
        Map<Long, List<JudgeEvaluation>> judgeMap = evaluations.stream()
                .filter(e -> e.getJudgeId() != null && e.getScore() != null)
                .collect(Collectors.groupingBy(JudgeEvaluation::getJudgeId));

        // Calculate global mean & global standard deviation
        double globalMean = evaluations.stream().mapToDouble(e -> e.getScore() != null ? e.getScore() : 70.0).average().orElse(75.0);
        double globalVariance = evaluations.stream().mapToDouble(e -> Math.pow((e.getScore() != null ? e.getScore() : 70.0) - globalMean, 2)).average().orElse(25.0);
        double globalStdDev = Math.max(1.0, Math.sqrt(globalVariance));

        List<Map<String, Object>> calibratedResults = new ArrayList<>();

        for (Map.Entry<Long, List<JudgeEvaluation>> entry : judgeMap.entrySet()) {
            Long judgeId = entry.getKey();
            List<JudgeEvaluation> judgeScores = entry.getValue();

            double judgeMean = judgeScores.stream().mapToDouble(JudgeEvaluation::getScore).average().orElse(globalMean);
            double judgeVariance = judgeScores.stream().mapToDouble(e -> Math.pow(e.getScore() - judgeMean, 2)).average().orElse(0.0);
            double judgeStdDev = Math.max(0.5, Math.sqrt(judgeVariance)); // prevent div by zero

            for (JudgeEvaluation eval : judgeScores) {
                double rawScore = eval.getScore();
                // Compute Z-score
                double zScore = (rawScore - judgeMean) / judgeStdDev;

                // Normalized calibrated score
                double calibratedScore = globalMean + (zScore * globalStdDev);
                calibratedScore = Math.max(0.0, Math.min(100.0, Math.round(calibratedScore * 10.0) / 10.0));

                // Update submission's rating
                Submission sub = submissionRepository.findById(eval.getSubmissionId()).orElse(null);
                if (sub != null) {
                    sub.setJudgeRating(calibratedScore / 10.0); // 10-point scale for UI
                    submissionRepository.save(sub);
                }

                calibratedResults.add(Map.of(
                    "judgeId", judgeId,
                    "submissionId", eval.getSubmissionId(),
                    "rawScore", rawScore,
                    "zScore", Math.round(zScore * 100.0) / 100.0,
                    "calibratedScore", calibratedScore
                ));
            }
        }

        log.info("Z-Score score calibration completed. Processed {} evaluations across {} judges.", evaluations.size(), judgeMap.size());

        return Map.of(
            "globalMean", Math.round(globalMean * 10.0) / 10.0,
            "globalStdDev", Math.round(globalStdDev * 10.0) / 10.0,
            "totalJudges", judgeMap.size(),
            "totalEvaluationsCalibrated", calibratedResults.size(),
            "calibratedEvaluations", calibratedResults
        );
    }
}
