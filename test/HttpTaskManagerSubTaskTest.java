import com.google.gson.Gson;
import manager.service.InMemoryTaskManager;
import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubTaskTest {
    TaskManager manager = new InMemoryTaskManager(Managers.getHistoryManager());
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.gson;

    public HttpTaskManagerSubTaskTest() throws IOException {
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
    public void ShouldAddSubTaskWithCorrectId() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание эпика 1");
        manager.createEpic(epic1);
        epic1.setStartTime(LocalDateTime.now());
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic1.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(5));
        String taskJson = gson.toJson(subTask1);
        System.out.println(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        ArrayList<Task> tasksFromManager = manager.getAllSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Количество задач не совпадает с фактическим");
    }

    @Test
    public void ShouldReturn406WhenAddingTimeOverlapSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание эпика 1");
        manager.createEpic(epic1);
        epic1.setStartTime(LocalDateTime.now());
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic1.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(5));
        manager.createSubTask(subTask1);
        subTask1.setDuration(Duration.ofMinutes(30));
        String taskJson = gson.toJson(subTask1);
        System.out.println(taskJson);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

        ArrayList<Task> tasksFromManager = manager.getAllSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Количество задач не совпадает с фактическим");
    }
}

