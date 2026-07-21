package com.example.demo.service;

import com.example.demo.dto.response.GithubRepoInfoDTO;

public interface GithubApiService {
    GithubRepoInfoDTO fetchRepositoryDetails(String githubRepoUrl);
}
