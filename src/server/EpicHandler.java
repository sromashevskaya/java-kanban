package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.gson;
import static server.HttpTaskServer.taskManager;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

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
                case 2 -> sendText(exchange, taskManager.getAllEpicTasks().toString());
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    Epic epic = taskManager.getEpicById(id);
                    sendText(exchange, String.valueOf(epic));
                }
                case 4 -> {
                    int id = parseId(splitPath[2]);
                    String subTasksJson = gson.toJson(taskManager.getSubTaskById(id));
                    sendText(exchange, subTasksJson);
                }
                default -> sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(requestBody, Epic.class);
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        int pathLength = splitPath.length;

        try {
            switch (pathLength) {
                case 2 -> {
                    taskManager.createEpic(epic);
                    sendText(exchange, epic.toString());
                }
                case 3 -> {
                    taskManager.updateEpic(epic);
                    sendText(exchange, epic.toString());
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
                    taskManager.deleteAllEpicTasks();
                    sendText(exchange, "Все эпики удалены");
                }
                case 3 -> {
                    int id = parseId(splitPath[2]);
                    taskManager.deleteEpicById(id);
                    sendText(exchange, "Указанный эипк удален");
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
            throw new RuntimeException("Указанный эпик не найден. Проверьте передаваемый id");
        }
    }
}