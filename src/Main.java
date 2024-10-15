import manager.history.FileBackedTaskManager;
import manager.service.Managers;
import manager.service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        File file = new File("sprint7.csv");
        FileBackedTaskManager fileManager = new FileBackedTaskManager(Managers.getHistoryManager(), file);

        Task task = new Task("Автоматизировать механизм возврата платежей", "Выставить ручку, которая будет отвечать за возврат платежей клиенту", Status.NEW);

        Epic epic = new Epic("Подключить нового провайдера", "Выставить ручку, которая будет отвечать за возврат платежей клиенту");

        SubTask subTask = new SubTask("Доработать модель данных", "Добавить новые поля", Status.NEW, 1);

        fileManager.createTask(task);
        fileManager.createEpic(epic);
        fileManager.createSubTask(subTask);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpicTasks()) {
            System.out.println(epic);

            for (int task : manager.getEpicSubtasks(epic.getTaskId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
