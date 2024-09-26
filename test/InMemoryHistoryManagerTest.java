import static org.junit.jupiter.api.Assertions.*;

import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;


class InMemoryHistoryManagerTest {
    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldAddTaskToHistoryAndReturnNullFromHistory() {
        // Создание задач
        Task task1 = new Task("Увеличить размерность поля", "Увеличить размерность поля description в таблице payments", Status.NEW);
        Task task2 = new Task("Сделать параметр не обязательным", "Сделать параметр не обязательным в сервисе getPayments", Status.NEW);

        // Добавление задач в менеджер задач
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Получение задачи по ID
        taskManager.getTaskById(task1.getTaskId());
        taskManager.getTaskById(task2.getTaskId());

        // Проверка, что история не пуста
        assertNotNull(taskManager.getHistory(), "В итории нет задач, история пуста");
    }

    @Test
    void shouldDeleteTaskAndReturnNullFromHistory() {
        // Создание задачи и добавление её в историю
        Task task = new Task("Увеличить размерность поля", "Увеличить размерность поля description в таблице invoices", Status.NEW);
        taskManager.createTask(task);
        taskManager.getTaskById(task.getTaskId());

        // Удаление задачи и проверка, что она удалена из истории
        taskManager.deleteTaskById(task.getTaskId());
        assertTrue(taskManager.getHistory().isEmpty(), "Задача не была удалена из истории");
    }
}