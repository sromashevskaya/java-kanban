import static org.junit.jupiter.api.Assertions.*;

import manager.service.Managers;
import manager.service.TaskManager;
import org.junit.jupiter.api.Test;

class ManagersTest {

    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void ShouldManagerBeNotNull() {
        assertNotNull(taskManager, "Утилитарный класс возвращает проинициализированные и готовые к работе экземпляры");
    }
}