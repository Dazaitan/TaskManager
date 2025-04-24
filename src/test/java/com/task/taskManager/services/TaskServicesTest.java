package com.task.taskManager.services;

import com.task.taskManager.entity.Task;
import com.task.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
class TaskServicesTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServices taskServices;
    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
        taskServices = new TaskServices(taskRepository); // Configuración del servicio con el repositorio simulado
    }

    @Test
    void testGetAllTasks() {
        // Configurar datos simulados
        List<Task> mockTasks = Arrays.asList(
                new Task(1L, "Tarea 1", "Descripción 1", false, LocalDateTime.now(), null),
                new Task(2L, "Tarea 2", "Descripción 2", true, LocalDateTime.now(), LocalDateTime.now())
        );
        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Llamar al método
        List<Task> tasks = taskServices.getAllTasks();

        // Verificar resultados
        assertEquals(2, tasks.size());
        assertEquals("Tarea 1", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreateTask() {
        // Configurar datos simulados
        Task task = new Task(1L, "Nueva Tarea", "Descripción nueva", false, LocalDateTime.now(), null);
        when(taskRepository.save(task)).thenReturn(task);

        // Llamar al método
        Task savedTask = taskServices.createTask(task);

        // Verificar resultados
        assertNotNull(savedTask);
        assertEquals("Nueva Tarea", savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testDeleteTask() {
        // Simular la acción de eliminar
        doNothing().when(taskRepository).deleteById(1L);

        // Llamar al método
        taskServices.deleteTask(1L);

        // Verificar invocación
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCloseExpiredTasks() {
        // Configurar tareas expiradas simuladas
        LocalDateTime now = LocalDateTime.now();
        List<Task> expiredTasks = Arrays.asList(
                new Task(1L, "Tarea expirada 1", "Descripción 1", false, now.minusDays(1), null),
                new Task(2L, "Tarea expirada 2", "Descripción 2", false, now.minusDays(2), null)
        );
        when(taskRepository.findByDueDateBeforeAndCompletedFalse(now)).thenReturn(expiredTasks);

        // Llamar al método
        taskServices.closeExpiredTasks(now);

        // Verificar cambios
        for (Task task : expiredTasks) {
            assertTrue(task.isCompleted());
            assertNotNull(task.getClosedAt());
            verify(taskRepository, times(1)).save(task);
        }
    }

    @Test
    void testCheckTaskStatusFoundActive() {
        // Configurar tarea simulada
        Task task = new Task(1L, "Tarea activa", "Descripción activa", false, LocalDateTime.now(), null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Llamar al método
        String status = taskServices.checkTaskStatus(1L);

        // Verificar resultados
        assertEquals("Tarea activa", status);
    }

    @Test
    void testCheckTaskStatusFoundClosed() {
        // Configurar tarea cerrada simulada
        Task task = new Task(1L, "Tarea cerrada", "Descripción cerrada", true, LocalDateTime.now(), LocalDateTime.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Llamar al método
        String status = taskServices.checkTaskStatus(1L);

        // Verificar resultados
        assertEquals("Se cerró la tarea: Tarea cerrada", status);
    }

    @Test
    void testCheckTaskStatusNotFound() {
        // Configurar tarea no encontrada
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método
        String status = taskServices.checkTaskStatus(1L);

        // Verificar resultados
        assertEquals("❌ Tarea no encontrada", status);
    }
}