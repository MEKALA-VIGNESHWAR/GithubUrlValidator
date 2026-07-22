package com.example.demo.controller;

import com.example.demo.entity.Team;
import com.example.demo.entity.TeamMember;
import com.example.demo.repository.TeamMemberRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    public TeamController(TeamRepository teamRepository, TeamMemberRepository teamMemberRepository) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable Long id) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) return ResponseEntity.notFound().build();
        List<TeamMember> members = teamMemberRepository.findByTeamId(id);
        return ResponseEntity.ok(Map.of("team", team, "members", members));
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Team team) {
        if (teamRepository.existsByTeamName(team.getTeamName())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Team name already exists"));
        }
        Team saved = teamRepository.save(team);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @RequestBody Team updated) {
        Team existing = teamRepository.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setTeamName(updated.getTeamName());
        existing.setLeaderName(updated.getLeaderName());
        existing.setCollege(updated.getCollege());
        existing.setPhoneNumber(updated.getPhoneNumber());

        return ResponseEntity.ok(teamRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamMemberRepository.deleteByTeamId(id);
        teamRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Team deleted successfully"));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMember(@PathVariable Long id, @RequestBody TeamMember member) {
        List<TeamMember> currentMembers = teamMemberRepository.findByTeamId(id);
        if (currentMembers.size() >= 4) {
            return ResponseEntity.badRequest().body(Map.of("message", "Maximum 4 members allowed per team"));
        }
        member.setTeamId(id);
        TeamMember saved = teamMemberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @PathVariable Long memberId) {
        teamMemberRepository.deleteById(memberId);
        return ResponseEntity.ok(Map.of("message", "Member removed"));
    }
}
