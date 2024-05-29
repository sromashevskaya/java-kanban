public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getHistoryManager());
    }

    static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
