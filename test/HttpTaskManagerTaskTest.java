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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
      //  Task task = new Task("Задача", "Описание задачи", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
      //  String taskJson = gson.toJson(task);
        String taskJson = "{\"name\":\"Test\",\"status\":\"NEW\",\"description\":\"Testing task 2\",\"duration\":\"PT5M\",\"startTime\":\"2024-08-10T01:06:50.4736799\"}";
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/task");
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

}



/*
public class HttpTaskManagerEpicTest {


    @Test
    public void ShouldAddEpicWithCorrectId() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание эпика");
        epic.setDuration(Duration.ofMinutes(30));
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpicTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Количество задач не совпадает с фактическим");
        assertEquals("Эпик", tasksFromManager.get(0).getName(), "Имя задачи не валидное");
    }


    @Test
    public void shouldReturn404WhenAddingEpicWithInvalidId() throws IOException, InterruptedException {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        epic.setDuration(Duration.ofMinutes(30));
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/invalid_epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался код ответа 404");
        List<Epic> tasksFromManager = manager.getAllEpicTasks();
        assertTrue(tasksFromManager.isEmpty(), "Список задач должен быть пустым");
    }

    @Test
    public void ShouldReturnAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание эпика 1");
        manager.createEpic(epic1);
        Epic epic2 = new Epic("Эпик2", "Описание эпика 2");
        manager.createEpic(epic2);
        epic1.setDuration(Duration.ofMinutes(1));
        epic2.setDuration(Duration.ofMinutes(2));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8081/epics");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpicTasks();

        assertNotNull(tasksFromManager, "Задачи не добавлены");
        assertEquals(2, manager.getAllEpicTasks().size(), "Количество задач не совпадает с фактическим");
    }

}
 */