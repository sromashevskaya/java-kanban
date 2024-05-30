import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.SubTask;

class SubTaskTest {

    private final SubTask subTask1 = new SubTask("Добавить новые поля", "Добавить поле референс платежа", Status.NEW);
    private final SubTask subTask2 = new SubTask("Добавить новые поля", "Добавить поле референс платежа", Status.NEW);


    private final int id1 = subTask1.getTaskId();
    private final int id2 = subTask2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(subTask1, subTask2);
    }
}