package com.example.TaskManagement.controller;

import com.example.TaskManagement.model.Task;
import com.example.TaskManagement.model.User;
import com.example.TaskManagement.service.TaskService;
import com.example.TaskManagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // Create a new task (authenticated users)
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        task.setOwner(user);
        Task saved = taskService.save(task);
        return ResponseEntity.ok(saved);
    }

    // Get all tasks (Admins see all, Users see only their own)
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(taskService.findAll());
        } else {
            return ResponseEntity.ok(taskService.findByOwner(user));
        }
    }

    // Get a specific task by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        return taskService.findById(id).map(task -> {
            boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
            if (isAdmin || (task.getOwner() != null && task.getOwner().getId().equals(user.getId()))) {
                return ResponseEntity.ok(task);
            }
            return ResponseEntity.status(403).body("Forbidden");
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a task (owner or admin)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody Task input, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        return taskService.findById(id).map(task -> {
            boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
            if (!isAdmin && (task.getOwner() == null || !task.getOwner().getId().equals(user.getId()))) {
                return ResponseEntity.status(403).body("Forbidden");
            }
            task.setTitle(input.getTitle());
            task.setDescription(input.getDescription());
            task.setDueDate(input.getDueDate());
            task.setStatus(input.getStatus());
            Task updated = taskService.save(task);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a task (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if (taskService.findById(id).isPresent()) {
            taskService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
