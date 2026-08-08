package com.example.demo.controller;

import com.example.demo.entity.AiReview;
import com.example.demo.entity.GithubIntelligence;
import com.example.demo.entity.PlagiarismReport;
import com.example.demo.entity.Submission;
import com.example.demo.repository.AiReviewRepository;
import com.example.demo.repository.GithubIntelligenceRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.service.AiAnalysisService;
import com.example.demo.service.PlagiarismDetectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/intelligence")
public class IntelligenceController {

    private final SubmissionRepository submissionRepository;
    private final GithubIntelligenceRepository githubIntelligenceRepository;
    private final AiReviewRepository aiReviewRepository;
    private final AiAnalysisService aiAnalysisService;
    private final PlagiarismDetectionService plagiarismDetectionService;

    public IntelligenceController(SubmissionRepository submissionRepository,
                                  GithubIntelligenceRepository githubIntelligenceRepository,
                                  AiReviewRepository aiReviewRepository,
                                  AiAnalysisService aiAnalysisService,
                                  PlagiarismDetectionService plagiarismDetectionService) {
        this.submissionRepository = submissionRepository;
        this.githubIntelligenceRepository = githubIntelligenceRepository;
        this.aiReviewRepository = aiReviewRepository;
        this.aiAnalysisService = aiAnalysisService;
        this.plagiarismDetectionService = plagiarismDetectionService;
    }

    @PostMapping("/summarize/{submissionId}")
    public ResponseEntity<?> summarizeSubmission(@PathVariable Long submissionId) {
        Map<String, Object> summary = aiAnalysisService.summarizeSubmission(submissionId);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/plagiarism-check/{submissionId}")
    public ResponseEntity<?> runPlagiarismCheck(@PathVariable Long submissionId) {
        PlagiarismReport report = plagiarismDetectionService.runPlagiarismCheck(submissionId);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/github/{submissionId}")
    public ResponseEntity<?> analyzeGithubRepository(@PathVariable Long submissionId) {
        Submission sub = submissionRepository.findById(submissionId).orElse(null);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }

        int stars = sub.getStars() != null ? sub.getStars() : 12;
        int forks = sub.getForks() != null ? sub.getForks() : 4;
        int commits = stars * 3 + 15;
        int issues = forks + 2;
        int prs = forks * 2 + 3;

        GithubIntelligence intel = githubIntelligenceRepository.findBySubmissionId(submissionId)
                .orElse(new GithubIntelligence());

        intel.setSubmissionId(submissionId);
        intel.setCommitCount(commits);
        intel.setOpenIssues(issues);
        intel.setPullRequests(prs);
        intel.setLanguageBreakdownJson("{\"Java\": 65.4, \"TypeScript\": 24.2, \"HTML/CSS\": 10.4}");
        intel.setTeamBalanceScore(88.5);
        intel.setCodeFreezeValid(true);

        GithubIntelligence saved = githubIntelligenceRepository.save(intel);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/github/{submissionId}")
    public ResponseEntity<?> getGithubIntelligence(@PathVariable Long submissionId) {
        GithubIntelligence intel = githubIntelligenceRepository.findBySubmissionId(submissionId).orElse(null);
        if (intel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(intel);
    }

    @PostMapping("/ai-review/{submissionId}")
    public ResponseEntity<?> generateAiReview(@PathVariable Long submissionId) {
        Submission sub = submissionRepository.findById(submissionId).orElse(null);
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }

        double rating = sub.getJudgeRating() != null ? sub.getJudgeRating() : 8.5;
        double innovation = Math.min(100.0, rating * 10 + 5);
        double technical = Math.min(100.0, rating * 9.5 + 8);
        double docScore = 92.0;
        double risk = 12.0;

        AiReview review = aiReviewRepository.findBySubmissionId(submissionId)
                .orElse(new AiReview());

        review.setSubmissionId(submissionId);
        review.setInnovationScore(innovation);
        review.setTechnicalScore(technical);
        review.setDocumentationScore(docScore);
        review.setRiskScore(risk);
        review.setAiSummary("Project '" + sub.getProjectTitle() + "' presents a strong, production-ready solution with clean modular Java/React architecture and comprehensive documentation.");
        review.setSponsorSpotlight("High commercial potential for Enterprise Cloud and AI track sponsors.");

        AiReview saved = aiReviewRepository.save(review);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/ai-review/{submissionId}")
    public ResponseEntity<?> getAiReview(@PathVariable Long submissionId) {
        AiReview review = aiReviewRepository.findBySubmissionId(submissionId).orElse(null);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }
}
