package manager.service;

import manager.history.HistoryManager;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> task = new HashMap<>();
    protected HashMap<Integer, SubTask> subTask = new HashMap<>();
    protected HashMap<Integer, Epic> epic = new HashMap<>();
    private int id = 0;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        historyManager.remove(id);

        prioritizedTasks = prioritizedTasks.stream()
                .filter(task -> task.getTaskType() != TaskType.TASK.TASK)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epic.values()) {
            epic.getSubTaskList().clear();
            updateStatus(epic);
            calculateDurationAndTime(epic);
        }
        subTask.clear();
        historyManager.remove(id);

        prioritizedTasks = prioritizedTasks.stream()
                .filter(task -> task.getTaskType() != TaskType.SUBTASK)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));

        epic.values().forEach(Epic::removeEpicSubTask);
    }

    @Override
    public void deleteAllEpicTasks() {
        epic.clear();
        subTask.clear();
        historyManager.remove(id);
        prioritizedTasks = prioritizedTasks.stream()
                .filter(task -> task.getTaskType() != TaskType.SUBTASK)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Task::getStartTime))));
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
        newTask.setTaskType(TaskType.TASK);
        this.task.put(newTask.getTaskId(), newTask);
        if (newTask.getStartTime() != null) {
            checkForTimeConflicts(newTask);
            prioritizedTasks.add(newTask);
        }
        return newTask.getTaskId();
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        newSubTask.setTaskId(id++);
        newSubTask.setTaskType(TaskType.SUBTASK);
        this.subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        newEpic.setSubTaskList(newSubTask);
        updateStatus(newEpic);
        calculateDurationAndTime(newEpic);
        if (newSubTask.getStartTime() != null) {
            checkForTimeConflicts(newSubTask);
            prioritizedTasks.add(newSubTask);
        }
        return newSubTask.getTaskId();
    }

    @Override
    public int createEpic(Epic newEpic) {
        newEpic.setTaskId(id++);
        newEpic.setTaskType(TaskType.EPIC);
        newEpic.setStatus(Status.NEW);
        this.epic.put(newEpic.getTaskId(), newEpic);
        if (newEpic.getStartTime() != null) {
            checkForTimeConflicts(newEpic);
            prioritizedTasks.add(newEpic);
        }
        return newEpic.getTaskId();
    }

    @Override
    public void updateTask(Task newTask) {
        task.put(newTask.getTaskId(), newTask);
        if (newTask.getStartTime() != null) {
            checkForTimeConflicts(newTask);
            prioritizedTasks.add(newTask);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        subTask.put(newSubTask.getTaskId(), newSubTask);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        updateStatus(newEpic);
        if (newSubTask.getStartTime() != null) {
            checkForTimeConflicts(newSubTask);
            prioritizedTasks.add(newSubTask);
        }
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
        if (task.containsKey(id)) {
            Task taskToRemove = task.get(id);
            prioritizedTasks.remove(taskToRemove);
            task.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask newSubTask = getSubTaskById(id);
        Epic newEpic = epic.get(newSubTask.getEpicById());
        calculateDurationAndTime(newEpic);
        prioritizedTasks.removeIf(subtask -> subtask.equals(getSubTaskById(id)));
        subTask.remove(id);
        historyManager.remove(id);
        updateStatus(newEpic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic newEpic = epic.get(id);
        if (newEpic != null) {
            newEpic.getSubTaskList().stream()
                    .forEach(subTask::remove);
            epic.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(id);
        }
    }

    @Override
    public ArrayList<Integer> getEpicSubtasks(int id) {
        Epic newEpic = epic.get(id);
        return newEpic.getSubTaskList();
    }

    private void updateStatus(Epic epic) {
        List<SubTask> subTasks = epic.getSubTaskList().stream()
                .map(subTaskId -> subTask.get(subTaskId))
                .collect(Collectors.toList());

        if (subTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean hasInProgress = subTasks.stream().anyMatch(subTask -> subTask.getStatus() == Status.IN_PROGRESS);
        boolean allDone = subTasks.stream().allMatch(subTask -> subTask.getStatus() == Status.DONE);
        boolean allNew = subTasks.stream().allMatch(subTask -> subTask.getStatus() == Status.NEW);

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (hasInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeOverlap(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task2.getEndTime().isBefore(task1.getStartTime());
    }

    public void checkForTimeConflicts(Task task) {
        getPrioritizedTasks().forEach(existingTask -> {
            if (hasTimeOverlap(existingTask, task)) {
                throw new IllegalArgumentException("В указанное время уже есть задача");
            }
        });
    }

    protected void calculateDurationAndTime(Epic epic) {
        if (epic.getSubTaskList().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ofSeconds(0));
            return;
        }

        List<SubTask> subTasksList = epic.getSubTaskList().stream()
                .map(subtaskId -> subTask.get(subtaskId))
                .collect(Collectors.toList());

        LocalDateTime startTime = subTasksList.stream()
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime endTime = subTasksList.stream()
                .map(SubTask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration totalDuration = subTasksList.stream()
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(totalDuration);
    }
}
