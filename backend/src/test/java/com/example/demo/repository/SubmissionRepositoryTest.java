package com.example.demo.repository;

import com.example.demo.entity.Submission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SubmissionRepositoryTest {

    @Autowired
    private SubmissionRepository repository;

    @Test
    void existsByTeamNameIgnoreCase_ReturnsTrueWhenExists() {
        Submission submission = new Submission("DevSquad", "Project X", "https://github.com/devsquad/project-x");
        repository.save(submission);

        assertTrue(repository.existsByTeamNameIgnoreCase("devsquad"));
        assertTrue(repository.existsByTeamNameIgnoreCase("DEVSQUAD"));
        assertTrue(repository.existsByTeamNameIgnoreCase("DevSquad"));
    }

    @Test
    void existsByTeamNameIgnoreCase_ReturnsFalseWhenNotExists() {
        assertFalse(repository.existsByTeamNameIgnoreCase("NonExistentTeam"));
    }
}
