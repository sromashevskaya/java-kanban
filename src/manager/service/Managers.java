package manager.service;

import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getHistoryManager());
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
