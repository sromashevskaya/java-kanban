package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;
import static server.HttpTaskServer.taskManager;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
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
                case 2 -> sendText(exchange, taskManager.getAllSubTasks().toString());
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    SubTask subTask = taskManager.getSubTaskById(id);
                    sendText(exchange, String.valueOf(subTask));
                }
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(requestBody, SubTask.class);
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int pathLength = splitPath.length;

        try {
            switch (pathLength) {
                case 2 -> {
                    taskManager.createSubTask(subTask);
                    sendText(exchange, subTask.toString());
                }
                case 3 -> {
                    taskManager.updateSubTask(subTask);
                    sendText(exchange, subTask.toString());
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
                    taskManager.deleteAllSubTasks();
                    sendText(exchange, "Все подзадачи удалены");
                }
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    taskManager.deleteSubTaskById(id);
                    sendText(exchange, "Указанная подзадача удалена");
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
            throw new RuntimeException("Указанная подзадача не найдена. Проверьте передаваемый id");
        }
    }


}