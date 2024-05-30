package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }

    public ArrayList<Integer> getSubTaskList() {
        return subTaskList;
    }

    public void removeSubTaskByEpicId(int id) {
        subTaskList.remove(id);
    }
}
