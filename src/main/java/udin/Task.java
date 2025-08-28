package udin;

public abstract class Task {
    protected String title;
    protected boolean isDone;

    public Task(String title) {
        this.title = title;
        this.isDone = false;
    }

    public String display() {
        return (isDone ? "[X] " : "[ ] ") + this.getTitle(); // mark done task with X
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getTitle() {
        return this.title;
    }

    public abstract String toSaveFormat();
}