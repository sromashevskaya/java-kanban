import static org.junit.jupiter.api.Assertions.*;

import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

class InMemoryTaskManagerTest {
    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void ShouldAddTaskAndCanFindItById() {
        Task task = new Task("Увеличить размерность поля", "Увеличить размерность описания платежа", Status.NEW);
        int newTask = taskManager.createTask(task);
        Task createdTask = taskManager.getTaskById(newTask);

        assertEquals(task, createdTask, "Задачи не совпадают");
    }

    @Test
    void ShouldAddSubTaskAndCanFindItById() {
        SubTask subTask = new SubTask("Оптимизировать запрос", "Оптимизировать запрос", Status.NEW);
        int newSubTask = taskManager.createTask(subTask);
        Task createdTask = taskManager.getTaskById(newSubTask);

        assertEquals(subTask, createdTask, "Задачи не совпадают");
        assertEquals(0, subTask.getTaskId(), "ID подзадач не совпадают");
    }

    @Test
    void ShouldBeCorrespondingParametersValues() {
        Task task = new Task("Задача", "Описание", Status.NEW);
        //  tasks.Task task2 = new tasks.Task("Задача1", "Описание2", tasks.Status.NEW);

        taskManager.createTask(task);

        assertEquals(task.getName(), "Задача", "Название задачи не совпадает с указанным");
        assertEquals(task.getDescription(), "Описание", "Описание задачи не совпадает с указанным");
        assertEquals(task.getStatus(), Status.NEW, "Статус задачи не совпадает с указанным");
    }

    @Test
    void ShouldBeCorrespondingIDsForSettedIdAndGeneratedId() {
        Task task = new Task("Задача", "Описание", Status.NEW);
        //  tasks.Task task2 = new tasks.Task("Задача1", "Описание2", tasks.Status.NEW);

        taskManager.createTask(task);

        assertEquals(task, taskManager.getTaskById(0), "Конфликт заданного и сгенерированного id");
    }

    @Test
    void ShouldPreviousTaskBeSavedWithNewOneCreation() {
        Task task1 = new Task("Задача1", "Описание1", Status.IN_PROGRESS);
        Task task2 = new Task("Задача2", "Описание2", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertEquals(task1.getName(), "Задача1", "Название предыдущей задачи изменилось");
        assertEquals(task1.getDescription(), "Описание1", "Описание предыдущей задачи изменилось");
        assertEquals(task1.getStatus(), Status.IN_PROGRESS, "Статус предыдущей задачи изменился");
    }

}