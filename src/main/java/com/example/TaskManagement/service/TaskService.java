package com.example.TaskManagement.service;

import com.example.TaskManagement.model.Task;
import com.example.TaskManagement.model.User;
import com.example.TaskManagement.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Create or Update Task
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    // Get all tasks
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    // Get task by ID
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // Delete task by ID
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    // Find tasks by owner
    public List<Task> findByOwner(User owner) {
        return taskRepository.findByOwner(owner);
    }
}
