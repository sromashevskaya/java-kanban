import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    HashMap<Integer, Task> task = new HashMap<>();
    HashMap<Integer, SubTask> subTask = new HashMap<>();
    HashMap<Integer, Epic> epic = new HashMap<>();
    private int id = 0;

    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(task.values());
    }

    @Override
    public ArrayList<Task> getAllSubTasks() {
        return new ArrayList<>(subTask.values());
    }

    @Override
    public ArrayList<Task> getAllEpicTasks() {
        return new ArrayList<>(epic.values());
    }

    @Override
    public void deleteAllTasks() {
        task.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epic.values()) {
            epic.getSubTaskList().clear();
            updateStatus(epic);
        }
        subTask.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epic.clear();
        subTask.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(task.get(id));
        return task.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTask.get(id));
        return subTask.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epic.get(id));
        return epic.get(id);
    }

    @Override
    public int createTask(Task newTask) {
        newTask.setTaskId(id++);
        task.put(newTask.getTaskId(), newTask);
        return newTask.getTaskId();
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        newSubTask.setTaskId(id++);
        subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        updateStatus(newEpic);
        return newSubTask.getTaskId();
    }

    @Override
    public int createEpic(Epic newEpic) {
        newEpic.setTaskId(id++);
        newEpic.setStatus(Status.NEW);
        this.epic.put(newEpic.getTaskId(), newEpic);
        return newEpic.getTaskId();
    }

    @Override
    public void updateTask(Task newTask) {
        task.put(newTask.getTaskId(), newTask);
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        updateStatus(newEpic);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        Epic updatedEpic = getEpicById(newEpic.getTaskId());
        if (updatedEpic == null) {
            return;
        }
        updatedEpic.setName(newEpic.getName());
        updatedEpic.setDescription(newEpic.getDescription());
        epic.put(updatedEpic.getTaskId(), updatedEpic);
    }

    @Override
    public void deleteTaskById(int id) {
        task.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask newSubTask = getSubTaskById(id);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        newEpic.removeSubTaskByEpicId(id);
        subTask.remove(id);
        updateStatus(newEpic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic newEpic = epic.get(id);
        for (Integer subTaskId : newEpic.getSubTaskList()) {
            subTask.remove(subTaskId);
        }
        epic.remove(id);
    }

    @Override
    public List<Integer> getEpicSubtasks(int id) {
        Epic newEpic = epic.get(id);
        return newEpic.getSubTaskList();
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
