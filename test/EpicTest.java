import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EpicTest {

    Epic epic1 = new Epic("Реализовать историю платежей", "Добавить новый сервис");
    Epic epic2 = new Epic("Реализовать историю платежей", "Добавить новый сервис");

    int id1 = epic1.getTaskId();
    int id2 = epic2.getTaskId();

    @Test
    void ShouldBeEqualId() {
        assertEquals(id1, id2);
        assertEquals(epic1, epic2);
    }
}