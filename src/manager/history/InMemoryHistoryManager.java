package manager.history;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> first;
    private Node<Task> last;

    private Map<Integer, Node<Task>> history = new LinkedHashMap<>();


    @Override
    public void add(Task task) {
        remove(task.getTaskId());
        linkLast(task);
    }

    private void linkLast(Task task) {
        final Node<Task> end = last;
        final Node<Task> start = new Node<>(task, null, end);
        last = start;
        history.put(task.getTaskId(), start);
        if (end == null) {
            first = start;
        } else {
            end.next = start;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        final Node<Task> next = node.next;
        final Node<Task> prev = node.prev;

        node.data = null;

        if (node == first) {
            first = next;
        }

        if (node == last) {
            last = prev;
        }

        if (prev != null) {
            prev.next = next;
        }

        if (next != null) {
            next.prev = prev;
        }
    }

    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> current = first;
        while (current != null) {
            history.add(current.data);
            current = current.next;
        }
        return history;
    }

    private class Node<T> {
        private T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
