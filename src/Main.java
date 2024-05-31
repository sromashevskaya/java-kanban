import manager.service.Managers;
import manager.service.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {


        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Автоматизировать механизм возврата платежей", "Выставить ручку, которая будет отвечать за возврат платежей клиенту", Status.NEW);
        taskManager.createTask(task1);

        SubTask subTask1 = new SubTask("Доработать модель данных", "Добавить новые поля", Status.NEW);

        Epic epic1 = new Epic("Подключить нового провайдера", "Выставить ручку, которая будет отвечать за возврат платежей клиенту");
        taskManager.createEpic(epic1);

        subTask1.setEpicId(epic1.getTaskId());
        taskManager.updateEpic(new Epic("Подключить нового провайдера", "Доработать модель данных", 1));
        taskManager.updateTask(new Task("Автоматизировать механизм возврата платежей", "Интегрироваться с корбанкингом", 1, Status.IN_PROGRESS));
        taskManager.createSubTask(subTask1);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);

        System.out.println(taskManager.getAllEpicTasks());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getEpicById(1));
        System.out.println(taskManager.getTaskById(1));

        //   taskManager.deleteAllTasks();
        //   taskManager.deleteAllEpicTasks();
        //   taskManager.deleteAllSubTasks();

        System.out.println(taskManager.getAllTasks());

        printAllTasks(taskManager);
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
