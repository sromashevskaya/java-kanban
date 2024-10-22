package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static server.HttpTaskServer.gson;
import static server.HttpTaskServer.taskManager;

public class TaskHistoryHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();

        if (HTTPMethods.valueOf(httpMethod) == HTTPMethods.GET) {
            handleGet(exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        if (!taskManager.getHistory().isEmpty()) {
            sendText(exchange, gson.toJson(taskManager.getHistory()));
        } else {
            sendNotFound(exchange);
        }
    }
}
