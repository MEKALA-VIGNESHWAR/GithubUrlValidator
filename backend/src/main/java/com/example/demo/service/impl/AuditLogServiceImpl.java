package com.example.demo.service.impl;

import com.example.demo.entity.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public void logAction(String action, String username, String entityType, Long entityId, String details) {
        AuditLog auditLog = new AuditLog(action, username, entityType, entityId, details);
        auditLogRepository.save(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAuditLogsByUser(String username, Pageable pageable) {
        return auditLogRepository.findByUsername(username, pageable);
    }
}
