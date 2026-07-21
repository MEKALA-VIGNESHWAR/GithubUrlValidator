package com.example.demo.service.impl;

import com.example.demo.dto.response.GithubRepoInfoDTO;
import com.example.demo.exception.InvalidSubmissionException;
import com.example.demo.service.GithubApiService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GithubApiServiceImpl implements GithubApiService {

    private final RestTemplate restTemplate;

    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(
            "^https?://(www\\.)?github\\.com/([A-Za-z0-9_.-]+)/([A-Za-z0-9_.-]+)/?$"
    );

    public GithubApiServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Override
    public GithubRepoInfoDTO fetchRepositoryDetails(String githubRepoUrl) {
        if (githubRepoUrl == null) {
            throw new InvalidSubmissionException("GitHub repository URL cannot be null.");
        }

        Matcher matcher = GITHUB_URL_PATTERN.matcher(githubRepoUrl.trim());
        if (!matcher.matches()) {
            throw new InvalidSubmissionException("A valid GitHub repository URL (e.g. https://github.com/owner/repo) is required.");
        }

        String owner = matcher.group(2);
        String repo = matcher.group(3);

        String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> data = response.getBody();
                Integer stars = data.get("stargazers_count") instanceof Number ? ((Number) data.get("stargazers_count")).intValue() : 0;
                Integer forks = data.get("forks_count") instanceof Number ? ((Number) data.get("forks_count")).intValue() : 0;
                Integer openIssues = data.get("open_issues_count") instanceof Number ? ((Number) data.get("open_issues_count")).intValue() : 0;
                String lastCommitDate = data.get("pushed_at") != null ? data.get("pushed_at").toString() : null;

                return new GithubRepoInfoDTO(repo, owner, stars, forks, openIssues, lastCommitDate);
            }
        } catch (Exception e) {
            // Graceful fallback if offline or GitHub rate limited
            return new GithubRepoInfoDTO(repo, owner, 0, 0, 0, null);
        }

        return new GithubRepoInfoDTO(repo, owner, 0, 0, 0, null);
    }
}
