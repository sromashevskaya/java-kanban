import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tasks.Epic;

class EpicTest {

    private final Epic epic1 = new Epic("Реализовать историю платежей", "Добавить новый сервис");
    private final Epic epic2 = new Epic("Реализовать историю платежей", "Добавить новый сервис");

    private final int id1 = epic1.getTaskId();
    private final int id2 = epic2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(epic1, epic2);
    }
}