package com.task.taskManager.controller;

import com.task.taskManager.entity.Task;
import com.task.taskManager.services.TaskServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskServices taskService;

    @Test
    void testGetAllTasks() throws Exception {
        // Configura las tareas simuladas
        List<Task> tasks = Arrays.asList(
                new Task(1L, "Tarea 1", "Descripción 1", false, LocalDateTime.now(), null),
                new Task(2L, "Tarea 2", "Descripción 2", true, LocalDateTime.now(), LocalDateTime.now())
        );
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Tarea 1"));
    }

    @Test
    void testCreateTask() throws Exception {
        Task task = new Task(1L, "Nueva Tarea", "Descripción nueva", false, LocalDateTime.now(), null);
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Nueva Tarea\", \"description\": \"Descripción nueva\", \"dueDate\": \"2025-04-22T18:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nueva Tarea"));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/task/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void testForceCloseExpiredTasks() throws Exception {
        // Simular el comportamiento del servicio
        doNothing().when(taskService).closeExpiredTasks(any(LocalDateTime.class));

        // Simular solicitud POST al endpoint y verificar respuesta
        mockMvc.perform(post("/task/close-task")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Verificar código HTTP 204

        // Verificar que el método del servicio fue llamado con el argumento correcto
        verify(taskService, times(1)).closeExpiredTasks(any(LocalDateTime.class));
    }
    @Test
    void testCheckTaskStatus() throws Exception {
        // Configurar datos simulados
        Long taskId = 1L;
        String mockStatus = "Tarea activa"; // Respuesta simulada del servicio
        when(taskService.checkTaskStatus(taskId)).thenReturn(mockStatus);

        // Simular solicitud GET al endpoint y verificar respuesta
        mockMvc.perform(get("/task/1/status") // Configuración del endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verificar código de estado HTTP 200
                .andExpect(content().string(mockStatus)); // Verificar contenido de la respuesta

        // Verificar que el servicio fue llamado una vez con el ID proporcionado
        verify(taskService, times(1)).checkTaskStatus(taskId);
    }
}