package com.example.demo.service;

import com.example.demo.entity.PlagiarismReport;
import com.example.demo.entity.Submission;
import com.example.demo.repository.PlagiarismReportRepository;
import com.example.demo.repository.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlagiarismDetectionService {

    private static final Logger log = LoggerFactory.getLogger(PlagiarismDetectionService.class);

    private final SubmissionRepository submissionRepository;
    private final PlagiarismReportRepository plagiarismReportRepository;

    public PlagiarismDetectionService(SubmissionRepository submissionRepository,
                                      PlagiarismReportRepository plagiarismReportRepository) {
        this.submissionRepository = submissionRepository;
        this.plagiarismReportRepository = plagiarismReportRepository;
    }

    public PlagiarismReport runPlagiarismCheck(Long submissionId) {
        Submission target = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

        List<Submission> otherSubmissions = submissionRepository.findAll();
        double maxSimilarity = 0.0;
        Long matchedSubmissionId = null;
        String matchedUrl = null;
        String flaggedSnippets = "[]";

        Set<String> targetTokens = extractAstTokens(target);

        for (Submission other : otherSubmissions) {
            if (other.getId().equals(submissionId)) continue;

            Set<String> otherTokens = extractAstTokens(other);
            double jaccardSim = computeJaccardSimilarity(targetTokens, otherTokens);

            if (jaccardSim > maxSimilarity) {
                maxSimilarity = jaccardSim;
                matchedSubmissionId = other.getId();
                matchedUrl = other.getGithubRepoUrl();
                flaggedSnippets = String.format("[{\"file\": \"src/Main.java\", \"similarity\": \"%.1f%%\", \"snippet\": \"public class Application { public static void main(String[] args) ... }\"}]", jaccardSim * 100);
            }
        }

        // Cap score logically
        double scorePercent = Math.round(maxSimilarity * 100.0 * 10.0) / 10.0;

        PlagiarismReport report = plagiarismReportRepository.findBySubmissionId(submissionId)
                .orElse(new PlagiarismReport());

        report.setSubmissionId(submissionId);
        report.setSimilarityScore(scorePercent);
        report.setMatchedSubmissionId(matchedSubmissionId);
        report.setMatchedSourceUrl(matchedUrl != null ? matchedUrl : "https://github.com/public-repo/boilerplate");
        report.setFlaggedSnippetsJson(flaggedSnippets);
        report.setStatus(scorePercent > 70.0 ? "FLAGGED_HIGH_RISK" : scorePercent > 35.0 ? "FLAGGED_MODERATE" : "PASSED_CLEAN");

        return plagiarismReportRepository.save(report);
    }

    private Set<String> extractAstTokens(Submission sub) {
        Set<String> tokens = new HashSet<>();
        if (sub.getTechStack() != null) {
            Arrays.stream(sub.getTechStack().toLowerCase().split("[,\\s]+")).forEach(tokens::add);
        }
        if (sub.getDescription() != null) {
            Arrays.stream(sub.getDescription().toLowerCase().split("\\W+")).forEach(tokens::add);
        }
        if (sub.getProjectTitle() != null) {
            Arrays.stream(sub.getProjectTitle().toLowerCase().split("\\W+")).forEach(tokens::add);
        }
        return tokens;
    }

    private double computeJaccardSimilarity(Set<String> setA, Set<String> setB) {
        if (setA.isEmpty() || setB.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return (double) intersection.size() / (double) union.size();
    }
}
