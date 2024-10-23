package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;
import static server.HttpTaskServer.taskManager;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();
        try {
            switch (HTTPMethods.valueOf(httpMethod)) {
                case GET -> handleGetRequest(exchange);
                case POST -> handlePostRequest(exchange);
                case DELETE -> handleDeleteRequest(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int pathLength = splitPath.length;

        try {
            switch (pathLength) {
                case 2 -> sendText(exchange, taskManager.getAllTasks().toString());
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    Task task = taskManager.getTaskById(id);
                    sendText(exchange, String.valueOf(task));
                }
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(requestBody, Task.class);
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int pathLength = splitPath.length;

        try {
            switch (pathLength) {
                case 2 -> {
                    taskManager.createTask(task);
                    sendText(exchange, task.toString());
                }
                case 3 -> {
                    taskManager.updateTask(task);
                    sendText(exchange, task.toString());
                }
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int pathLength = splitPath.length;

        try {
            switch (pathLength) {
                case 2 -> {
                    taskManager.deleteAllTasks();
                    sendText(exchange, "Все задачи удалены");
                }
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    taskManager.deleteTaskById(id);
                    sendText(exchange, "Указанная задача удалена");
                }
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    private int parseId(String idStr) throws NumberFormatException {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Указанная задача не найдена. Проверьте передаваемый id");
        }
    }


}