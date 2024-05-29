import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubTaskTest {

    SubTask subTask1 = new SubTask("Добавить новые поля", "Добавить поле референс платежа", Status.NEW);
    SubTask subTask2 = new SubTask("Добавить новые поля", "Добавить поле референс платежа", Status.NEW);


    int id1 = subTask1.getTaskId();
    int id2 = subTask2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(subTask1, subTask2);
    }
}