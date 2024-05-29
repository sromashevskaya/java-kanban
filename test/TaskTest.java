import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TaskTest {

    Task task1 = new Task("Доработать модель данных", "Добавить новые поля", Status.NEW);
    Task task2 = new Task("Доработать модель данных", "Добавить новые поля", Status.NEW);

    int id1 = task1.getTaskId();
    int id2 = task2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(task1, task2);
    }
}