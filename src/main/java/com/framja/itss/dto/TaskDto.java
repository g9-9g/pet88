package com.framja.itss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Long assigneeId;
    private String assigneeUsername;
    private LocalDateTime dueDate;
    private boolean completed;
    private LocalDateTime createdAt;
} 