package com.example.demo.controller;

import com.example.demo.entity.Hackathon;
import com.example.demo.repository.HackathonRepository;
import com.example.demo.service.EventScraperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping({ "/api/hackathons", "/api/v1/hackathons" })
@CrossOrigin(origins = "http://localhost:5173")
public class HackathonController {

    private final HackathonRepository hackathonRepository;
    private final EventScraperService eventScraperService;

    public HackathonController(HackathonRepository hackathonRepository, EventScraperService eventScraperService) {
        this.hackathonRepository = hackathonRepository;
        this.eventScraperService = eventScraperService;
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
            slug = slug + "-" + UUID.randomUUID().toString().substring(0, 5);
            h.setSlug(slug);
        }

        Hackathon saved = hackathonRepository.save(h);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/scrape")
    public ResponseEntity<?> scrapeEvents(@RequestBody Map<String, Object> body) {
        String url = (String) body.get("url");
        String source = (String) body.get("source");
        String query = (String) body.get("query");
        
        @SuppressWarnings("unchecked")
        List<String> interests = (List<String>) body.get("interests");

        List<Map<String, Object>> events = eventScraperService.scrapeEvents(url, source, query, interests);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importScrapedEvents(@RequestBody List<Map<String, Object>> eventsPayload) {
        if (eventsPayload == null || eventsPayload.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "No events provided for import"));
        }

        List<Hackathon> savedHackathons = new ArrayList<>();

        for (Map<String, Object> item : eventsPayload) {
            String title = (String) item.get("title");
            if (title == null || title.trim().isEmpty()) continue;

            String baseSlug = title.toLowerCase().replaceAll("[^a-z0-9]", "-");
            String slug = baseSlug;
            int counter = 1;
            while (hackathonRepository.existsBySlug(slug)) {
                slug = baseSlug + "-" + counter++;
            }

            Hackathon h = new Hackathon();
            h.setOrganizationId(1L);
            h.setTitle(title);
            h.setSlug(slug);

            String typeStr = (String) item.get("eventType");
            if (typeStr == null) typeStr = (String) item.get("type");
            h.setEventType(typeStr != null ? typeStr.toUpperCase() : "ONLINE");

            h.setDescription((String) item.get("description"));
            h.setOrganizer((String) item.get("organizer"));
            
            Object locObj = item.get("location");
            if (locObj instanceof String) {
                h.setLocation((String) locObj);
            } else if (locObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> locMap = (Map<String, Object>) locObj;
                String city = (String) locMap.get("city");
                Boolean isOnline = (Boolean) locMap.get("is_online");
                h.setLocation(isOnline != null && isOnline ? "Online / Remote" : (city != null ? city : "TBD"));
            }

            if (item.get("prizePool") != null) {
                h.setPrizePool((String) item.get("prizePool"));
            }
            if (item.get("difficulty") != null) {
                h.setDifficulty((String) item.get("difficulty"));
            }
            if (item.get("track") != null) {
                h.setTrack((String) item.get("track"));
            }

            h.setStartDate(LocalDateTime.now().plusDays(3));
            h.setEndDate(LocalDateTime.now().plusDays(10));
            h.setSubmissionDeadline(LocalDateTime.now().plusDays(9));
            h.setIsPublished(true);

            Hackathon saved = hackathonRepository.save(h);
            savedHackathons.add(saved);
        }

        return ResponseEntity.ok(Map.of(
            "message", "Successfully imported " + savedHackathons.size() + " events to Events page!",
            "importedCount", savedHackathons.size(),
            "events", savedHackathons
        ));
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

