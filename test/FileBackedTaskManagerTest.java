import static org.junit.jupiter.api.Assertions.*;

import manager.history.FileBackedTaskManager;
import manager.service.Managers;
import org.junit.jupiter.api.Test;
import manager.service.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

public class FileBackedTaskManagerTest {

    @Test
    void ShouldSaveAndLoadAnEmptyFile() throws IOException {
        File file = File.createTempFile("emptyFile", ".csv");
        manager.history.FileBackedTaskManager fileManager = new manager.history.FileBackedTaskManager(Managers.getHistoryManager(), file);
        fileManager.save();

        assertEquals(0, fileManager.getAllTasks().size(), "Сохранение и загрузка пустого файла прошла неуспешно");
        assertEquals(0, fileManager.getAllSubTasks().size(), "Сохранение и загрузка пустого файла прошла неуспешно");
        assertEquals(0, fileManager.getAllEpicTasks().size(), "Сохранение и загрузка пустого файла прошла неуспешно");
    }

    @Test
    void ShouldSaveSeveralTasks() {
        File file = null;
        try {
            file = File.createTempFile("saveSeveralTasks", ".csv");
            manager.history.FileBackedTaskManager fileManager = new manager.history.FileBackedTaskManager(Managers.getHistoryManager(), file);

            Task task1 = new Task("Автоматизировать механизм возврата платежей", "Выставить ручку, которая будет отвечать за возврат платежей клиенту", Status.NEW);
            Epic epic1 = new Epic("Подключить нового провайдера", "Выставить ручку, которая будет отвечать за возврат платежей клиенту");

            fileManager.createTask(task1);
            fileManager.createEpic(epic1);

            Task task2 = new Task("Составить список провайдеров", "Составить список всех платежных провайдеров", Status.NEW);
            Epic epic2 = new Epic("Автоматизировать работу бэк-офиса", "Ускорить сверку отчетов", 2);

            fileManager.createTask(task2);
            fileManager.createEpic(epic2);

            assertEquals(2, fileManager.getAllTasks().size(), "Сохранение нескольких задач прошло неуспешно");
            assertEquals(2, fileManager.getAllEpicTasks().size(), "Сохранение нескольких эпиков прошло неуспешно");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    @Test
    void ShouldLoadSeveralTasks() {
        File file = null;
        try {
            file = File.createTempFile("loadSeveralTasks", ".csv");
            manager.history.FileBackedTaskManager fileManager = new manager.history.FileBackedTaskManager(Managers.getHistoryManager(), file);

            Task task1 = new Task("Автоматизировать механизм возврата платежей", "Выставить ручку, которая будет отвечать за возврат платежей клиенту", Status.NEW);
            Epic epic1 = new Epic("Подключить нового провайдера", "Выставить ручку, которая будет отвечать за возврат платежей клиенту");

            fileManager.createTask(task1);
            fileManager.createEpic(epic1);

            manager.history.FileBackedTaskManager fileManagerNew = FileBackedTaskManager.loadFromFile(file);

            assertEquals(fileManagerNew, fileManager, "Загрузка нескольких задач прошла неуспешно");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании временного файла", e);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }
}
