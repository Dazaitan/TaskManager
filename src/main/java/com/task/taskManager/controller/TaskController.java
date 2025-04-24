package com.task.taskManager.controller;

import com.task.taskManager.entity.Task;
import com.task.taskManager.services.TaskServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskServices taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> checkTaskStatus(@PathVariable Long id) {
        String status = taskService.checkTaskStatus(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PostMapping("/close-task")
    public ResponseEntity<Void> forceCloseExpiredTasks() {
        taskService.closeExpiredTasks(LocalDateTime.now()); // Ejecuta el cierre manualmente
        return ResponseEntity.noContent().build(); // Responde con HTTP 204
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
