import static org.junit.jupiter.api.Assertions.*;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

class ManagersTest {

    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void ShouldManagerBeNotNull() {
        assertNotNull(taskManager, "Утилитарный класс возвращает проинициализированные и готовые к работе экземпляры");
    }
}