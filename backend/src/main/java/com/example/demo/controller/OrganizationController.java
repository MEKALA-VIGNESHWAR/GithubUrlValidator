package com.example.demo.controller;

import com.example.demo.entity.Organization;
import com.example.demo.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long id) {
        Organization org = organizationRepository.findById(id).orElse(null);
        if (org == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(org);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getOrganizationBySlug(@PathVariable String slug) {
        Organization org = organizationRepository.findBySlug(slug.toLowerCase()).orElse(null);
        if (org == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(org);
    }

    @PostMapping
    public ResponseEntity<?> createOrganization(@RequestBody Organization org) {
        if (org.getName() == null || org.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Organization name is required"));
        }

        String slug = org.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = org.getName().toLowerCase().replaceAll("[^a-z0-9]", "-");
        }
        org.setSlug(slug);

        if (organizationRepository.existsBySlug(slug)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Organization slug already exists"));
        }

        Organization saved = organizationRepository.save(org);
        return ResponseEntity.ok(saved);
    }
}
