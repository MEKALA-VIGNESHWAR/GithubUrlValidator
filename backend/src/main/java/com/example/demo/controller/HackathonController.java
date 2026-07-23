package com.example.demo.controller;

import com.example.demo.entity.Hackathon;
import com.example.demo.repository.HackathonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({ "/api/hackathons", "/api/v1/hackathons" })
@CrossOrigin(origins = "http://localhost:5173")
public class HackathonController {

    private final HackathonRepository hackathonRepository;

    public HackathonController(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    @GetMapping
    public ResponseEntity<List<Hackathon>> getAllPublishedHackathons() {
        return ResponseEntity.ok(hackathonRepository.findByIsPublishedTrue());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Hackathon>> getHackathonsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hackathonRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHackathonById(@PathVariable Long id) {
        Hackathon h = hackathonRepository.findById(id).orElse(null);
        if (h == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getHackathonBySlug(@PathVariable String slug) {
        Hackathon h = hackathonRepository.findBySlug(slug.toLowerCase()).orElse(null);
        if (h == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(h);
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<Hackathon>> getHackathonsByOrganization(@PathVariable Long orgId) {
        return ResponseEntity.ok(hackathonRepository.findByOrganizationId(orgId));
    }

    @PostMapping
    public ResponseEntity<?> createHackathon(@RequestBody Hackathon h) {
        if (h.getTitle() == null || h.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Hackathon title is required"));
        }

        String slug = h.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = h.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "-");
        }
        h.setSlug(slug);

        if (hackathonRepository.existsBySlug(slug)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Hackathon slug already exists"));
        }

        Hackathon saved = hackathonRepository.save(h);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHackathon(@PathVariable Long id, @RequestBody Hackathon updated) {
        Hackathon existing = hackathonRepository.findById(id).orElse(null);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (updated.getTitle() != null)
            existing.setTitle(updated.getTitle());
        if (updated.getEventType() != null)
            existing.setEventType(updated.getEventType());
        if (updated.getDescription() != null)
            existing.setDescription(updated.getDescription());
        if (updated.getRules() != null)
            existing.setRules(updated.getRules());
        if (updated.getPrizes() != null)
            existing.setPrizes(updated.getPrizes());
        if (updated.getSubmissionDeadline() != null)
            existing.setSubmissionDeadline(updated.getSubmissionDeadline());
        if (updated.getIsPublished() != null)
            existing.setIsPublished(updated.getIsPublished());

        Hackathon saved = hackathonRepository.save(existing);
        return ResponseEntity.ok(saved);
    }
}
