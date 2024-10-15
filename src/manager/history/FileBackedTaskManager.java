package manager.history;

import exceptions.ManagerSaveException;
import manager.service.InMemoryTaskManager;
import manager.service.Managers;
import tasks.*;

import java.io.*;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    private static final String header = "id,type,name,status,description,epic";

    private static String fileName = "sprint7.csv";


    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public int createTask(Task newTask) {
        int taskId = super.createTask(newTask);
        save();
        return taskId;
    }

    @Override
    public int createEpic(Epic newEpic) {
        int epicId = super.createEpic(newEpic);
        save();
        return epicId;
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        int subTaskId = super.createSubTask(newSubTask);
        save();
        return subTaskId;
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }


    public void save() {
        try (Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(header + "\n");

            saveTasks(writer);

            saveEpics(writer);

            saveSubTasks(writer);

        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить данные", exception);
        }
    }

    private void saveTasks(Writer writer) throws IOException {
        for (Task task : task.values()) {
            writer.write(task.toString() + "\n");
        }
    }

    private void saveEpics(Writer writer) throws IOException {
        for (Epic epic : epic.values()) {
            writer.write(epic.toString() + "\n");
        }
    }

    private void saveSubTasks(Writer writer) throws IOException {
        for (SubTask subTask : subTask.values()) {
            writer.write(subTask.toString() + "," + subTask.getEpicById() + "\n");
        }
    }

    private String toString(Task task) {
        TaskType type;
        String epicId = "";

        if (task instanceof SubTask subTask) {
            epicId = String.valueOf(subTask.getEpicById());
            type = TaskType.SUBTASK;
        } else if (task instanceof Epic) {
            type = TaskType.EPIC;
        } else {
            type = TaskType.TASK;
        }

        return String.join(",", String.valueOf(task.getTaskId()), type.toString(), task.getName(), task.getStatus().toString(), task.getDescription(), epicId) + "\n";
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        int id = Integer.parseInt(split[0]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String description = split[4];

        Task task;

        TaskType type = TaskType.valueOf(split[1]);

        switch (type) {
            case TASK:
                task = new Task(name, description, status);
                break;
            case EPIC:
                task = new Epic(name, description, id, status);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(split[5]);
                task = new SubTask(name, description, status, epicId);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        task.setTaskId(id);
        task.setTaskType(type);

        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getHistoryManager(), file);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.equals(fileBackedTaskManager.header)) {
                    continue;
                }

                Task task = fileBackedTaskManager.fromString(line);

                switch (task.getTaskType()) {
                    case TASK -> fileBackedTaskManager.createTask(task);
                    case EPIC -> fileBackedTaskManager.createEpic((Epic) task);
                    case SUBTASK -> fileBackedTaskManager.createSubTask((SubTask) task);
                    default -> throw new IllegalStateException("Неизвестный тип задачи: " + task.getTaskType());
                }
            }

            return fileBackedTaskManager;
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось восстановить данные из файла", exception);
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBackedTaskManager that = (FileBackedTaskManager) o;
        return Objects.equals(file, that.file) && Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, header);
    }
}
