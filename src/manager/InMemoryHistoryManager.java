package manager;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
//    private final List<tasks.Task> history = new ArrayList<>();

    private final LinkedList<Task> history = new LinkedList<>();
    private final static int MAX_SIZE = 10;


    @Override
    public void add(Task task) {
        if (history.size() >= MAX_SIZE) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
