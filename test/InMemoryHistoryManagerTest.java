import static org.junit.jupiter.api.Assertions.*;

import manager.history.HistoryManager;
import manager.service.Managers;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getHistoryManager();


    @Test
    void ShouldPreviousTaskBeSavedWithNewOneCreationInHistoryManager() {
        Task task1 = new Task("Задача1", "Описание1", Status.IN_PROGRESS);
        Task task2 = new Task("Задача2", "Описание2", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();

        assertEquals(history.get(0), task1, "Не сооветствие предыдущей задачи при добавлении новой");
    }
}