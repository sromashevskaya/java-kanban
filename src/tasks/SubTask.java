package tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
    }

    public int getEpicById() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
}
