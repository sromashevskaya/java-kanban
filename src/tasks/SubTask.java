package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicById() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return taskId +
                "," + getTaskType().toString() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getDuration() +
                "," + getStartTime();
    }
}
