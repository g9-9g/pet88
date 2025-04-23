package com.framja.itss.service;

import com.framja.itss.entity.Task;
import com.framja.itss.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task);
    Optional<Task> getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByAssignee(User assignee);
    List<Task> getOverdueTasks();
    List<Task> searchTasksByTitle(String title);
    Task updateTask(Task task);
    void deleteTask(Long id);
    void markTaskAsCompleted(Long id);
} 