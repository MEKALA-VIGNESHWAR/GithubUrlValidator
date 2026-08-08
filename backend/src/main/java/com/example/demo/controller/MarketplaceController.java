package com.example.demo.controller;

import com.example.demo.entity.SkillCard;
import com.example.demo.repository.SkillCardRepository;
import com.example.demo.service.MatchmakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    private final SkillCardRepository skillCardRepository;
    private final MatchmakingService matchmakingService;

    public MarketplaceController(SkillCardRepository skillCardRepository, MatchmakingService matchmakingService) {
        this.skillCardRepository = skillCardRepository;
        this.matchmakingService = matchmakingService;
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<SkillCard>> getAllSkillCards() {
        return ResponseEntity.ok(skillCardRepository.findByLookingForTeamTrue());
    }

    @PostMapping("/skill-card")
    public ResponseEntity<?> createOrUpdateSkillCard(@RequestBody SkillCard skillCard) {
        if (skillCard.getUserId() == null || skillCard.getFullName() == null || skillCard.getPrimaryRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId, fullName, and primaryRole are required"));
        }

        SkillCard existing = skillCardRepository.findByUserId(skillCard.getUserId()).orElse(null);
        if (existing != null) {
            existing.setFullName(skillCard.getFullName());
            existing.setEmail(skillCard.getEmail());
            existing.setPrimaryRole(skillCard.getPrimaryRole());
            existing.setSkillsJson(skillCard.getSkillsJson());
            existing.setTimezone(skillCard.getTimezone());
            existing.setBio(skillCard.getBio());
            existing.setGithubUrl(skillCard.getGithubUrl());
            existing.setLinkedinUrl(skillCard.getLinkedinUrl());
            existing.setLookingForTeam(skillCard.getLookingForTeam() != null ? skillCard.getLookingForTeam() : true);
            return ResponseEntity.ok(skillCardRepository.save(existing));
        }

        return ResponseEntity.ok(skillCardRepository.save(skillCard));
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<?> getRecommendedTeammates(@PathVariable Long userId) {
        List<Map<String, Object>> recommendations = matchmakingService.getRecommendedTeammates(userId);
        return ResponseEntity.ok(recommendations);
    }
}
