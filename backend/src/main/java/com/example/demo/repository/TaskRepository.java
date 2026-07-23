package com.example.demo.repository;

import com.example.demo.entity.TaskEntity;
import com.example.demo.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByProjectIdAndDeletedFalse(Long projectId);
    List<TaskEntity> findByProjectIdAndStatusAndDeletedFalse(Long projectId, TaskStatus status);
}
