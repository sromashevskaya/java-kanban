import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ManagersTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    void ShouldManagerBeNotNull() {
        assertNotNull(taskManager, "Утилитарный класс возвращает проинициализированные и готовые к работе экземпляры");
    }
}