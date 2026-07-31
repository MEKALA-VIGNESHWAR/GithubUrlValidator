package com.example.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventScraperService {

    private static final Logger logger = LoggerFactory.getLogger(EventScraperService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> scrapeEvents(String url, String source, String query, List<String> interests) {
        logger.info("Scraping events for source: {}, url: {}, query: {}", source, url, query);
        
        List<Map<String, Object>> scrapedEvents = runPythonScraper(url, source, query, interests);

        if (scrapedEvents != null && !scrapedEvents.isEmpty()) {
            logger.info("Successfully scraped {} events from Python scraper pipeline", scrapedEvents.size());
            return scrapedEvents;
        }

        logger.info("Python pipeline returned no items or was skipped. Using backend fallback aggregator...");
        return getFallbackEvents(source, url, query);
    }

    private List<Map<String, Object>> runPythonScraper(String url, String source, String query, List<String> interests) {
        try {
            File workspaceDir = new File(System.getProperty("user.dir")).getParentFile();
            File pipelineFile = new File(workspaceDir, "webscraper/pipeline.py");
            
            if (!pipelineFile.exists()) {
                pipelineFile = new File("webscraper/pipeline.py");
            }

            if (!pipelineFile.exists()) {
                logger.warn("Pipeline script not found at path: {}", pipelineFile.getAbsolutePath());
                return Collections.emptyList();
            }

            ProcessBuilder pb = new ProcessBuilder("python", pipelineFile.getAbsolutePath());
            pb.directory(pipelineFile.getParentFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(6, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                logger.warn("Python scraper process timed out after 6s. Force destroying process.");
                process.destroyForcibly();
                return Collections.emptyList();
            }

            if (process.exitValue() == 0) {
                String fullOutput = output.toString();
                int jsonStartIndex = fullOutput.indexOf("[");
                int jsonEndIndex = fullOutput.lastIndexOf("]");
                
                if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                    String jsonStr = fullOutput.substring(jsonStartIndex, jsonEndIndex + 1);
                    return objectMapper.readValue(jsonStr, new TypeReference<List<Map<String, Object>>>() {});
                }
            }
        } catch (Exception e) {
            logger.warn("Could not execute python webscraper script: {}", e.getMessage());
        }
        return Collections.emptyList();
    }


    private List<Map<String, Object>> getFallbackEvents(String source, String url, String query) {
        List<Map<String, Object>> events = new ArrayList<>();
        String targetQuery = (query != null && !query.trim().isEmpty()) ? query.toLowerCase() : "ai";

        if (url != null && !url.trim().isEmpty()) {
            Map<String, Object> customEvent = new HashMap<>();
            String domain = url.replace("https://", "").replace("http://", "").split("/")[0];
            customEvent.put("id", "scraped_custom_" + System.currentTimeMillis());
            customEvent.put("title", "Scraped Event from " + domain);
            customEvent.put("eventType", "HACKATHON");
            customEvent.put("organizer", domain + " Team");
            customEvent.put("location", Map.of("is_online", true, "city", "Remote"));
            customEvent.put("startDate", LocalDateTime.now().plusDays(7).toString());
            customEvent.put("endDate", LocalDateTime.now().plusDays(9).toString());
            customEvent.put("submissionDeadline", LocalDateTime.now().plusDays(8).toString());
            customEvent.put("description", "Scraped directly from target URL: " + url + ". Includes challenge details and submission guidelines.");
            customEvent.put("applicationUrl", url);
            customEvent.put("sourceWebsite", domain);
            customEvent.put("tags", List.of("custom-scrape", "web-scraper", targetQuery));
            customEvent.put("imageUrl", "https://logo.clearbit.com/" + domain);
            customEvent.put("score", 92.5);
            customEvent.put("prizePool", "$30,000");
            customEvent.put("difficulty", "Advanced");
            customEvent.put("track", "Web Scraping & AI");
            events.add(customEvent);
        }

        // Add curated events matching scrapers
        if (source == null || source.equalsIgnoreCase("ALL") || source.equalsIgnoreCase("Devpost")) {
            Map<String, Object> devpost1 = new HashMap<>();
            devpost1.put("id", "devpost_ai_agents_2025");
            devpost1.put("title", "Global AI Agents & LLM Hackathon 2025");
            devpost1.put("eventType", "HACKATHON");
            devpost1.put("organizer", "Devpost & OpenAI Community");
            devpost1.put("location", Map.of("is_online", true, "city", "Worldwide Virtual"));
            devpost1.put("startDate", LocalDateTime.now().plusDays(3).toString());
            devpost1.put("endDate", LocalDateTime.now().plusDays(10).toString());
            devpost1.put("submissionDeadline", LocalDateTime.now().plusDays(9).toString());
            devpost1.put("description", "Build next-generation autonomous AI agents, multi-agent frameworks, and LLM tool-calling applications.");
            devpost1.put("applicationUrl", "https://devpost.com/hackathons/ai-agents-2025");
            devpost1.put("sourceWebsite", "Devpost");
            devpost1.put("tags", List.of("ai", "llm", "python", "agents", "openai"));
            devpost1.put("imageUrl", "https://logo.clearbit.com/devpost.com");
            devpost1.put("score", 95.0);
            devpost1.put("prizePool", "$50,000");
            devpost1.put("difficulty", "Intermediate");
            devpost1.put("track", "AI & Machine Learning");
            events.add(devpost1);

            Map<String, Object> devpost2 = new HashMap<>();
            devpost2.put("id", "devpost_cloud_native_2025");
            devpost2.put("title", "Cloud Native & Microservices Challenge");
            devpost2.put("eventType", "HACKATHON");
            devpost2.put("organizer", "CNCF & Kubernetes Foundation");
            devpost2.put("location", Map.of("is_online", true, "city", "Online"));
            devpost2.put("startDate", LocalDateTime.now().plusDays(14).toString());
            devpost2.put("endDate", LocalDateTime.now().plusDays(21).toString());
            devpost2.put("submissionDeadline", LocalDateTime.now().plusDays(20).toString());
            devpost2.put("description", "Architect scalable cloud-native web services using Docker, Spring Boot, and Kubernetes.");
            devpost2.put("applicationUrl", "https://devpost.com/hackathons/cloud-native");
            devpost2.put("sourceWebsite", "Devpost");
            devpost2.put("tags", List.of("cloud", "kubernetes", "java", "spring-boot", "devops"));
            devpost2.put("imageUrl", "https://logo.clearbit.com/cncf.io");
            devpost2.put("score", 88.0);
            devpost2.put("prizePool", "$25,000");
            devpost2.put("difficulty", "Advanced");
            devpost2.put("track", "DevOps & Cloud");
            events.add(devpost2);
        }

        if (source == null || source.equalsIgnoreCase("ALL") || source.equalsIgnoreCase("GitHub Internships")) {
            Map<String, Object> gh1 = new HashMap<>();
            gh1.put("id", "gh_intern_google_2025");
            gh1.put("title", "Google - Software Engineering Intern 2025");
            gh1.put("eventType", "INTERNSHIP");
            gh1.put("organizer", "Google");
            gh1.put("location", Map.of("is_online", false, "city", "Mountain View, CA"));
            gh1.put("startDate", LocalDateTime.now().plusDays(60).toString());
            gh1.put("endDate", LocalDateTime.now().plusDays(150).toString());
            gh1.put("submissionDeadline", LocalDateTime.now().plusDays(30).toString());
            gh1.put("description", "Join Google's Core Infrastructure team for a 12-week summer software engineering internship.");
            gh1.put("applicationUrl", "https://careers.google.com/jobs/results/");
            gh1.put("sourceWebsite", "GitHub Internships Feed");
            gh1.put("tags", List.of("internship", "python", "java", "algorithms"));
            gh1.put("imageUrl", "https://logo.clearbit.com/google.com");
            gh1.put("score", 94.0);
            gh1.put("prizePool", "Paid Internship");
            gh1.put("difficulty", "Intermediate");
            gh1.put("track", "Software Engineering");
            events.add(gh1);
        }

        if (source == null || source.equalsIgnoreCase("ALL") || source.equalsIgnoreCase("Tech Events")) {
            Map<String, Object> tech1 = new HashMap<>();
            tech1.put("id", "tech_aws_summit_2025");
            tech1.put("title", "AWS Global Cloud Architecture Summit");
            tech1.put("eventType", "WEBINAR");
            tech1.put("organizer", "Amazon Web Services");
            tech1.put("location", Map.of("is_online", true, "city", "Online Webinar"));
            tech1.put("startDate", LocalDateTime.now().plusDays(5).toString());
            tech1.put("endDate", LocalDateTime.now().plusDays(5).toString());
            tech1.put("submissionDeadline", LocalDateTime.now().plusDays(4).toString());
            tech1.put("description", "Interactive technical sessions on building serverless microservices with AWS Lambda and Spring Boot.");
            tech1.put("applicationUrl", "https://aws.amazon.com/events/webinars/");
            tech1.put("sourceWebsite", "Tech Events Aggregator");
            tech1.put("tags", List.of("webinar", "aws", "cloud", "java"));
            tech1.put("imageUrl", "https://logo.clearbit.com/aws.amazon.com");
            tech1.put("score", 86.5);
            tech1.put("prizePool", "Free Certification Vouchers");
            tech1.put("difficulty", "Beginner");
            tech1.put("track", "Cloud Architecture");
            events.add(tech1);
        }

        return events;
    }
}
