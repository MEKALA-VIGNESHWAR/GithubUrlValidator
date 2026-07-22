package com.example.demo.repository;

import com.example.demo.entity.Hackathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HackathonRepository extends JpaRepository<Hackathon, Long> {
    List<Hackathon> findByOrganizationId(Long organizationId);
    Optional<Hackathon> findBySlug(String slug);
    List<Hackathon> findByIsPublishedTrue();
    Boolean existsBySlug(String slug);
}
