package com.example.demo.repository;

import com.example.demo.entity.SkillCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillCardRepository extends JpaRepository<SkillCard, Long> {
    Optional<SkillCard> findByUserId(Long userId);
    List<SkillCard> findByLookingForTeamTrue();
    List<SkillCard> findByPrimaryRoleContainingIgnoreCase(String primaryRole);
}
