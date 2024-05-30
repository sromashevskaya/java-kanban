import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

class TaskTest {

    private final Task task1 = new Task("Доработать модель данных", "Добавить новые поля", Status.NEW);
    private final Task task2 = new Task("Доработать модель данных", "Добавить новые поля", Status.NEW);

    private final int id1 = task1.getTaskId();
    private final int id2 = task2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(task1, task2);
    }
}