import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    HashMap<Integer, Task> task = new HashMap<>();
    HashMap<Integer, SubTask> subTask = new HashMap<>();
    HashMap<Integer, Epic> epic = new HashMap<>();
    private int id = 0;

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(task.values());
    }

    public ArrayList<Task> getAllSubTasks() {
        return new ArrayList<>(subTask.values());
    }

    public ArrayList<Task> getAllEpicTasks() {
        return new ArrayList<>(epic.values());
    }

    public void deleteAllTasks() {
        task.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epic.values()) {
            epic.getSubTaskList().clear();
            updateStatus(epic);
        }
        subTask.clear();
    }

    public void deleteAllEpicTasks() {
        epic.clear();
        subTask.clear();
    }

    public Task getTaskById(int id) {
        return task.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTask.get(id);
    }

    public Epic getEpicById(int id) {
        return epic.get(id);
    }

    public int createTask(Task newTask) {
        newTask.setTaskId(id++);
        this.task.put(newTask.getTaskId(), newTask);
        return newTask.getTaskId();
    }

    public int createSubTask(SubTask newSubTask) {
        newSubTask.setTaskId(id++);
        subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        updateStatus(newEpic);
        return newSubTask.getTaskId();
    }

    public int createEpic(Epic newEpic) {
        newEpic.setTaskId(id++);
        newEpic.setStatus(Status.NEW);
        this.epic.put(newEpic.getTaskId(), newEpic);
        return newEpic.getTaskId();
    }

    public void updateTask(Task newTask) {
        task.put(newTask.getTaskId(), newTask);
    }

    public void updateSubTask(SubTask newSubTask) {
        subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic= epic.get(newSubTask.getEpicById());
        updateStatus(newEpic);
    }

    public void updateEpic(Epic newEpic) {
        Epic updatedEpic = getEpicById(newEpic.getTaskId());
        if (updatedEpic == null) {
            return;
        }
        updatedEpic.setName(newEpic.getName());
        updatedEpic.setDescription(newEpic.getDescription());
        epic.put(updatedEpic.getTaskId(), updatedEpic);
    }

    public void deleteTaskById(int id) {
        task.remove(id);
    }

    public void deleteSubTaskById(int id) {
        SubTask newSubTask = getSubTaskById(id);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        newEpic.removeSubTaskByEpicId(id);
        subTask.remove(id);
        updateStatus(newEpic);
    }

    public void deleteEpicById(int id) {
        Epic newEpic= epic.get(id);
        for (Integer subTaskId : newEpic.getSubTaskList()) {
            subTask.remove(subTaskId);
        }
        epic.remove(id);
    }

    private void updateStatus(Epic epic) {
        int count = 0;

        if (epic.getSubTaskList().isEmpty()) {
            epic.setStatus(Status.NEW);
        }
        for (Integer subTaskId : epic.getSubTaskList()) {
            if (subTask.get(subTaskId).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTask.get(subTaskId).getStatus() == Status.DONE) {
                count++;
            }
        }
        if (count == epic.getSubTaskList().size()) {
            epic.setStatus(Status.DONE);
        }
    }

}
