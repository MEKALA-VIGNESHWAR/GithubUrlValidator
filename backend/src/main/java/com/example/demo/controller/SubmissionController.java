package com.example.demo.controller;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionResponseDTO submitProject(@Valid @RequestBody SubmissionRequestDTO requestDTO) {
        return submissionService.submitProject(requestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Object getAllSubmissions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "submittedAt") String sort,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            @RequestParam(required = false) String team) {

        if (team != null && !team.trim().isEmpty()) {
            return submissionService.searchSubmissionsByTeam(team.trim());
        }

        if (page != null && size != null) {
            Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
            return submissionService.getAllSubmissions(pageable);
        }

        return submissionService.getAllSubmissionsList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubmissionResponseDTO getSubmission(@PathVariable Long id) {
        return submissionService.getSubmissionById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SubmissionResponseDTO updateSubmission(@PathVariable Long id, @Valid @RequestBody SubmissionRequestDTO requestDTO) {
        return submissionService.updateSubmission(id, requestDTO);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public SubmissionResponseDTO updateStatus(@PathVariable Long id, @RequestParam SubmissionStatus status) {
        return submissionService.updateSubmissionStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<SubmissionResponseDTO> searchByTeam(@RequestParam String team) {
        return submissionService.searchSubmissionsByTeam(team);
    }
}