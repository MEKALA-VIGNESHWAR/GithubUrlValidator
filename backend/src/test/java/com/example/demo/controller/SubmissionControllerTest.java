package com.example.demo.controller;

import com.example.demo.dto.request.SubmissionRequestDTO;
import com.example.demo.dto.response.SubmissionResponseDTO;
import com.example.demo.enums.SubmissionStatus;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@org.springframework.test.context.ActiveProfiles("test")
class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubmissionService submissionService;

    private SubmissionRequestDTO validRequest;
    private SubmissionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        validRequest = new SubmissionRequestDTO("TeamOne", "App Title", "https://github.com/teamone/app", "Leader", "leader@example.com", "MIT");
        responseDTO = new SubmissionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTeamName("TeamOne");
        responseDTO.setProjectTitle("App Title");
        responseDTO.setGithubRepoUrl("https://github.com/teamone/app");
        responseDTO.setStatus(SubmissionStatus.PENDING);
    }

    @Test
    void submitProject_ReturnsCreatedStatus() throws Exception {
        when(submissionService.submitProject(any(SubmissionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teamName").value("TeamOne"))
                .andExpect(jsonPath("$.projectTitle").value("App Title"));
    }

    @Test
    void submitProject_DuplicateTeam_ReturnsConflict() throws Exception {
        when(submissionService.submitProject(any(SubmissionRequestDTO.class)))
                .thenThrow(new ConflictException("Team 'TeamOne' has already submitted a project."));

        mockMvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getAllSubmissions_ReturnsOk() throws Exception {
        when(submissionService.getAllSubmissionsList()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/submissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamName").value("TeamOne"));
    }

    @Test
    void getSubmission_Success() throws Exception {
        when(submissionService.getSubmissionById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/submissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teamName").value("TeamOne"));
    }

    @Test
    void getSubmission_NotFound_Returns404() throws Exception {
        when(submissionService.getSubmissionById(99L))
                .thenThrow(new ResourceNotFoundException("Submission with ID 99 was not found."));

        mockMvc.perform(get("/api/submissions/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteSubmission_Success_ReturnsNoContent() throws Exception {
        doNothing().when(submissionService).deleteSubmission(1L);

        mockMvc.perform(delete("/api/submissions/1"))
                .andExpect(status().isNoContent());
    }
}
