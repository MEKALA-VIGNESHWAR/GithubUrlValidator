package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_checkins")
public class QrCheckin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long hackathonId;

    @Column(nullable = false)
    private String qrCodeHash;

    @Column(nullable = false)
    private String scanType; // ENTRY, MEAL, SWAG, SESSION

    private Long scannedBy;

    private LocalDateTime scannedAt;

    public QrCheckin() {
        this.scanType = "ENTRY";
        this.scannedAt = LocalDateTime.now();
    }

    public QrCheckin(Long userId, Long hackathonId, String qrCodeHash, String scanType, Long scannedBy) {
        this.userId = userId;
        this.hackathonId = hackathonId;
        this.qrCodeHash = qrCodeHash;
        this.scanType = scanType != null ? scanType.toUpperCase() : "ENTRY";
        this.scannedBy = scannedBy;
        this.scannedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getHackathonId() { return hackathonId; }
    public void setHackathonId(Long hackathonId) { this.hackathonId = hackathonId; }

    public String getQrCodeHash() { return qrCodeHash; }
    public void setQrCodeHash(String qrCodeHash) { this.qrCodeHash = qrCodeHash; }

    public String getScanType() { return scanType; }
    public void setScanType(String scanType) { this.scanType = scanType; }

    public Long getScannedBy() { return scannedBy; }
    public void setScannedBy(Long scannedBy) { this.scannedBy = scannedBy; }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }
}
