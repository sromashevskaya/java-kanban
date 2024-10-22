package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.service.Managers;
import manager.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8081;
    private final HttpServer httpServer;
    public static TaskManager taskManager;
    public static Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        taskManager = manager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/subtasks", new SubTaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/history", new TaskHistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedTaskListHandler());
    }

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public void stop() {
        httpServer.stop(0);
    }

    public void start() {
        httpServer.start();
    }
}