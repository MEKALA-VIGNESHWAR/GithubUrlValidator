package com.example.demo.service;

import com.example.demo.entity.Hackathon;
import com.example.demo.enums.HackathonStatus;
import com.example.demo.repository.HackathonRepository;
import com.example.demo.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasksService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasksService.class);

    private final HackathonRepository hackathonRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public ScheduledTasksService(HackathonRepository hackathonRepository, RefreshTokenRepository refreshTokenRepository) {
        this.hackathonRepository = hackathonRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // Runs every 5 minutes to transition hackathon statuses
    @Scheduled(cron = "0 */5 * * * *")
    public void updateHackathonStatuses() {
        logger.info("Executing scheduled hackathon status transition check...");
        LocalDateTime now = LocalDateTime.now();

        List<Hackathon> hackathons = hackathonRepository.findAll();
        for (Hackathon h : hackathons) {
            if (h.getStartDate() != null && h.getEndDate() != null) {
                if (now.isBefore(h.getStartDate()) && h.getStatus() != HackathonStatus.UPCOMING) {
                    h.setStatus(HackathonStatus.UPCOMING);
                    hackathonRepository.save(h);
                } else if (now.isAfter(h.getStartDate()) && now.isBefore(h.getEndDate()) && h.getStatus() != HackathonStatus.LIVE) {
                    h.setStatus(HackathonStatus.LIVE);
                    hackathonRepository.save(h);
                } else if (now.isAfter(h.getEndDate()) && h.getStatus() != HackathonStatus.COMPLETED) {
                    h.setStatus(HackathonStatus.COMPLETED);
                    hackathonRepository.save(h);
                }
            }
        }
    }

    // Runs nightly at 3:00 AM to purge expired refresh tokens
    @Scheduled(cron = "0 0 3 * * *")
    public void purgeExpiredTokens() {
        logger.info("Purging expired refresh tokens...");
        // Additional cleanup logic
    }
}
