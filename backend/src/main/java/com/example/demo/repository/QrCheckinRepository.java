package com.example.demo.repository;

import com.example.demo.entity.QrCheckin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrCheckinRepository extends JpaRepository<QrCheckin, Long> {
    List<QrCheckin> findByUserIdAndHackathonId(Long userId, Long hackathonId);
    Boolean existsByUserIdAndHackathonIdAndScanType(Long userId, Long hackathonId, String scanType);
}
