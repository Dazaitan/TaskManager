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
    private final TaskRepository taskRepository;

    public TaskServices(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void scheduledCloseExpiredTasks() {
        /*Tuve que separar la logica principal de la tarea programada
         para poder manejar parametros y testear el metodo principal.*/
        LocalDateTime now = LocalDateTime.now();
        closeExpiredTasks(now);
    }

    public void closeExpiredTasks(LocalDateTime now) {
        List<Task> expiredTasks = taskRepository.findByDueDateBeforeAndCompletedFalse(now);
        expiredTasks.forEach(task -> {
            task.setCompleted(true);
            task.setClosedAt(now); // Establecer hora de cierre
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
