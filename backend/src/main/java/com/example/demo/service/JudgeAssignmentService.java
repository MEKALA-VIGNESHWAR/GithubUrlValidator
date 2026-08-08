package com.example.demo.service;

import com.example.demo.entity.JudgeExpertise;
import com.example.demo.entity.Submission;
import com.example.demo.repository.JudgeExpertiseRepository;
import com.example.demo.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JudgeAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(JudgeAssignmentService.class);

    private final SubmissionRepository submissionRepository;
    private final JudgeExpertiseRepository judgeExpertiseRepository;

    public JudgeAssignmentService(SubmissionRepository submissionRepository,
                                  JudgeExpertiseRepository judgeExpertiseRepository) {
        this.submissionRepository = submissionRepository;
        this.judgeExpertiseRepository = judgeExpertiseRepository;
    }

    public Map<String, Object> autoAssignSubmissionsToJudges() {
        List<Submission> submissions = submissionRepository.findAll();
        List<JudgeExpertise> judges = judgeExpertiseRepository.findAll();

        if (judges.isEmpty()) {
            // Seed a default judge profile if none exists
            JudgeExpertise defaultJudge1 = judgeExpertiseRepository.save(new JudgeExpertise(101L, "[\"AI/ML\", \"Web3\", \"Enterprise\"]", 15));
            JudgeExpertise defaultJudge2 = judgeExpertiseRepository.save(new JudgeExpertise(102L, "[\"FinTech\", \"DevOps\", \"Mobile\"]", 15));
            judges = List.of(defaultJudge1, defaultJudge2);
        }

        int assignedCount = 0;
        List<Map<String, Object>> assignments = new ArrayList<>();

        for (Submission sub : submissions) {
            String category = sub.getCategory() != null ? sub.getCategory() : "General";

            // Find optimal judge with matching expertise tag & lowest workload
            JudgeExpertise bestJudge = null;
            double bestScore = -1.0;

            for (JudgeExpertise judge : judges) {
                if (judge.getCurrentWorkload() >= judge.getMaxWorkload()) continue;

                boolean tagMatch = judge.getExpertiseTagsJson() != null &&
                        judge.getExpertiseTagsJson().toLowerCase().contains(category.toLowerCase());

                double matchScore = (tagMatch ? 50.0 : 10.0) + (judge.getMaxWorkload() - judge.getCurrentWorkload());

                if (matchScore > bestScore) {
                    bestScore = matchScore;
                    bestJudge = judge;
                }
            }

            if (bestJudge != null) {
                bestJudge.setCurrentWorkload(bestJudge.getCurrentWorkload() + 1);
                judgeExpertiseRepository.save(bestJudge);
                assignedCount++;

                assignments.add(Map.of(
                    "submissionId", sub.getId(),
                    "projectTitle", sub.getProjectTitle(),
                    "category", category,
                    "assignedJudgeUserId", bestJudge.getUserId(),
                    "judgeWorkload", bestJudge.getCurrentWorkload() + "/" + bestJudge.getMaxWorkload()
                ));
            }
        }

        log.info("Assigned {} submissions to {} judges based on expertise tags and workload balancing.", assignedCount, judges.size());

        return Map.of(
            "totalSubmissionsAssigned", assignedCount,
            "judgesInPool", judges.size(),
            "assignmentDetails", assignments
        );
    }
}
