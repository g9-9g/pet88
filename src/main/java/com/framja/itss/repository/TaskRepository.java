package com.framja.itss.repository;

import com.framja.itss.entity.Task;
import com.framja.itss.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User assignee);
    List<Task> findByCompletedFalseAndDueDateBefore(LocalDateTime dateTime);
    List<Task> findByTitleContainingIgnoreCase(String title);
} 