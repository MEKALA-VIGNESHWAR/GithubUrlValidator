package com.example.demo.controller;

import com.example.demo.entity.AuditLog;
import com.example.demo.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/admin/audit-logs", "/api/v1/admin/audit-logs"})
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String username
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (username != null && !username.isBlank()) {
            return ResponseEntity.ok(auditLogService.getAuditLogsByUser(username, pageRequest));
        }
        return ResponseEntity.ok(auditLogService.getAllAuditLogs(pageRequest));
    }
}
