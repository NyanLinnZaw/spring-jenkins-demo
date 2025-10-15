package com.example.TaskManagement.repository;

import com.example.TaskManagement.model.Task;
import com.example.TaskManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwner(User owner);
}
