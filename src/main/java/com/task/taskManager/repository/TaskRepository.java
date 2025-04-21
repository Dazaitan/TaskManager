package com.task.taskManager.repository;

import com.task.taskManager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository <Task, Long>{
    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDateTime now);
}
