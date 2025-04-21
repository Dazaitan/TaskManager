package com.task.taskManager.services;

import com.task.taskManager.entity.Task;
import com.task.taskManager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TaskServices {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    @Scheduled(fixedRate = 60000) // Ejecuta cada minuto
    public void closeExpiredTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> expiredTasks = taskRepository.findByDueDateBeforeAndCompletedFalse(LocalDateTime.now());
        expiredTasks.forEach(task -> {
            task.setCompleted(true);
            task.setClosedAt(now);
            taskRepository.save(task);
            System.out.println("Se cerró la tarea automáticamente: " + task.getTitle());
        });
    }
    public String checkTaskStatus(Long id) {
        return taskRepository.findById(id)
                .map(task -> task.isCompleted() ? "Se cerró la tarea: " + task.getTitle() : "Tarea activa")
                .orElse("❌ Tarea no encontrada");
    }
}
