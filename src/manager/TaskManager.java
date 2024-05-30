package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    ArrayList<Task> getAllTasks();

    ArrayList<Task> getAllSubTasks();

    ArrayList<Task> getAllEpicTasks();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpicTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    Epic getEpicById(int id);

    int createTask(Task newTask);

    int createSubTask(SubTask newSubTask);

    int createEpic(Epic newEpic);

    void updateTask(Task newTask);

    void updateSubTask(SubTask newSubTask);

    void updateEpic(Epic newEpic);

    void deleteTaskById(int id);

    void deleteSubTaskById(int id);

    void deleteEpicById(int id);

    List<Integer> getEpicSubtasks(int id);
}
