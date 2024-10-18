import static org.junit.jupiter.api.Assertions.*;

import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

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
    
    @Test
    public void shouldCheckSubTasksWithNewStatus() {
        final TaskManager taskManager1 = Managers.getDefault();
        taskManager1.deleteAllSubTasks();
        taskManager1.deleteAllEpicTasks();
        taskManager1.deleteAllTasks();
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager1.createEpic(epic);
        epic.setStartTime(LocalDateTime.now().plusMinutes(1));
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.NEW, epic.getTaskId(), Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(4));
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание 3", Status.NEW, epic.getTaskId(), Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(10));
        taskManager1.createSubTask(subTask1);
        taskManager1.createSubTask(subTask2);
        taskManager1.createSubTask(subTask3);
        assertSame(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldCheckSubTasksWithDoneStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        epic.setStartTime(LocalDateTime.now().plusMinutes(10));
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.DONE, epic.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.DONE, epic.getTaskId(), Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(4));
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание 3", Status.DONE, epic.getTaskId(), Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(10));
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);
        assertSame(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldCheckSubTasksWithNewAndDoneStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        epic.setStartTime(LocalDateTime.now().plusMinutes(10));
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic.getTaskId(), Duration.ofMinutes(2), LocalDateTime.now());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.DONE, epic.getTaskId(), Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(4));
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        assertSame(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldCheckSubTasksWithInProgressStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.createEpic(epic);
        epic.setStartTime(LocalDateTime.now().plusMinutes(10));
        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.IN_PROGRESS, epic.getTaskId(), Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(4));
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.IN_PROGRESS, epic.getTaskId(), Duration.ofMinutes(3), LocalDateTime.now().plusMinutes(10));
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);
        assertSame(epic.getStatus(), Status.IN_PROGRESS);
    }


}