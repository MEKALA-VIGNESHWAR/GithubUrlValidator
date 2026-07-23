package com.example.demo.service;

import com.example.demo.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    void logAction(String action, String username, String entityType, Long entityId, String details);
    Page<AuditLog> getAllAuditLogs(Pageable pageable);
    Page<AuditLog> getAuditLogsByUser(String username, Pageable pageable);
}
