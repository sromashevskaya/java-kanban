package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskList = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public Epic(String name, String description, int taskId, Status status) {
        super(name, description, taskId, status);
    }

    public Epic(String name, String description, int taskId, Status status, Duration duration, LocalDateTime startTime) {
        super(name, description, taskId, status, duration, startTime);
    }

    public void setSubTaskList(SubTask subTask) {
        subTaskList.add(subTask.getTaskId());
    }

    public ArrayList<Integer> getSubTaskList() {
        return new ArrayList<>(subTaskList);
    }

     public void removeSubTaskByEpicId(int id) {
        subTaskList.remove(id);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void removeEpicSubTask() {
        subTaskList.clear();
    }


}
