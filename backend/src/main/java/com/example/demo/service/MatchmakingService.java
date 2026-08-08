package com.example.demo.service;

import com.example.demo.entity.SkillCard;
import com.example.demo.repository.SkillCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    private static final Logger log = LoggerFactory.getLogger(MatchmakingService.class);

    private final SkillCardRepository skillCardRepository;

    public MatchmakingService(SkillCardRepository skillCardRepository) {
        this.skillCardRepository = skillCardRepository;
    }

    public List<Map<String, Object>> getRecommendedTeammates(Long userId) {
        List<SkillCard> cards = skillCardRepository.findByLookingForTeamTrue();
        Optional<SkillCard> currentCardOpt = skillCardRepository.findByUserId(userId);

        SkillCard currentCard = currentCardOpt.orElse(null);
        String currentRole = currentCard != null ? currentCard.getPrimaryRole() : "Developer";
        Set<String> currentSkills = currentCard != null ? parseSkills(currentCard.getSkillsJson()) : Set.of("Java", "React");

        List<Map<String, Object>> recommendations = new ArrayList<>();

        for (SkillCard card : cards) {
            if (card.getUserId().equals(userId)) continue;

            Set<String> otherSkills = parseSkills(card.getSkillsJson());
            double skillComplementarityScore = calculateComplementarity(currentRole, card.getPrimaryRole(), currentSkills, otherSkills);
            double timezoneScore = (card.getTimezone() != null && currentCard != null && card.getTimezone().equalsIgnoreCase(currentCard.getTimezone())) ? 20.0 : 10.0;

            double matchPercentage = Math.min(99.0, Math.max(60.0, Math.round((skillComplementarityScore + timezoneScore) * 10.0) / 10.0));

            recommendations.add(Map.of(
                "userId", card.getUserId(),
                "fullName", card.getFullName(),
                "email", card.getEmail(),
                "primaryRole", card.getPrimaryRole(),
                "skillsJson", card.getSkillsJson(),
                "timezone", card.getTimezone() != null ? card.getTimezone() : "UTC",
                "bio", card.getBio() != null ? card.getBio() : "Passionate developer looking to build high-impact projects.",
                "githubUrl", card.getGithubUrl() != null ? card.getGithubUrl() : "https://github.com",
                "matchPercentage", matchPercentage,
                "compatibilityTag", matchPercentage > 85.0 ? "HIGHLY_COMPATIBLE" : "COMPATIBLE"
            ));
        }

        // Sort by highest match percentage
        recommendations.sort((a, b) -> Double.compare((Double) b.get("matchPercentage"), (Double) a.get("matchPercentage")));

        return recommendations;
    }

    private Set<String> parseSkills(String json) {
        if (json == null || json.isBlank()) return Set.of();
        String cleaned = json.replaceAll("[\\[\\]\"']", "");
        return Arrays.stream(cleaned.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private double calculateComplementarity(String roleA, String roleB, Set<String> skillsA, Set<String> skillsB) {
        double score = 50.0;
        // Reward complementary roles (e.g. Frontend + Backend, ML + UI/UX)
        if (!roleA.equalsIgnoreCase(roleB)) {
            score += 25.0;
        }
        // Reward distinct, non-overlapping skillsets
        Set<String> combined = new HashSet<>(skillsA);
        combined.addAll(skillsB);

        score += Math.min(20.0, combined.size() * 3.0);
        return score;
    }
}
