import com.google.gson.Gson;
import manager.service.InMemoryTaskManager;
import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTaskTest {
    TaskManager manager = new InMemoryTaskManager(Managers.getHistoryManager());
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.gson;

    public HttpTaskManagerTaskTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpicTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void ShouldAddTaskWithCorrectId() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание задачи", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Количество задач не совпадает с фактическим");
        assertEquals("Задача", tasksFromManager.get(0).getName(), "Имя задачи не валидное");
    }

    @Test
    public void shouldReturn404WhenAddingTaskWithInvalidId() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание задачи", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/invalid_tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался код ответа 404");
        List<Task> tasksFromManager = manager.getAllTasks();
        assertTrue(tasksFromManager.isEmpty(), "Список задач должен быть пустым");
    }

    @Test
    public void ShouldReturnAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(1), LocalDateTime.now());
        manager.createTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 1", Status.NEW, Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(5));
        manager.createTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/tasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не добавлены");
        assertEquals(2, manager.getAllTasks().size(), "Количество задач не совпадает с фактическим");
    }

}