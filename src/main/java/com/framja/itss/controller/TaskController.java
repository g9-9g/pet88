package com.framja.itss.controller;

import com.framja.itss.dto.TaskDto;
import com.framja.itss.entity.Task;
import com.framja.itss.entity.User;
import com.framja.itss.service.TaskService;
import com.framja.itss.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    private final UserService userService;
    
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        System.out.println("Current user info:");
        System.out.println("Username: " + username);
        System.out.println("Authorities: " + authentication.getAuthorities());
        
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User ID: " + user.getId());
            System.out.println("Full Name: " + user.getFullName());
            System.out.println("Email: " + user.getEmail());
            
            return ResponseEntity.ok(userService.convertToDto(user));
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            List<Task> tasks = taskService.getTasksByAssignee(userOpt.get());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        List<Task> tasks = taskService.getOverdueTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String title) {
        List<Task> tasks = taskService.searchTasksByTitle(title);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (!id.equals(task.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return taskService.getTaskById(id)
                .map(existingTask -> {
                    Task updatedTask = taskService.updateTask(task);
                    return new ResponseEntity<>(updatedTask, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> markTaskAsCompleted(@PathVariable Long id) {
        if (taskService.getTaskById(id).isPresent()) {
            taskService.markTaskAsCompleted(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
} 