package udin;

/**
 * Represents an abstract task with a title and completion status.
 * <p>
 * A task can be marked as done or undone, displayed in a formatted
 * string, and converted into a save-friendly format for storage.
 */
public abstract class Task {
    /**
     * The title or description of the task.
     */
    protected String title;

    /**
     * Tracks whether the task has been completed.
     */
    protected boolean isDone;

    /**
     * Constructs a task with the given title.
     * The task is initially marked as not done.
     *
     * @param title the description or name of the task
     */
    public Task(String title) {
        this.title = title;
        this.isDone = false;
    }

    /**
     * Returns a string representation of the task in display format.
     * <p>
     * The format is:
     * <ul>
     *   <li>{@code [X] title} if the task is done</li>
     *   <li>{@code [ ] title} if the task is not done</li>
     * </ul>
     *
     * @return the formatted display string for this task
     */
    public String display() {
        return (isDone ? "[X] " : "[ ] ") + this.getTitle();
    }

    /**
     * Marks this task as completed.
     */
    public void mark() {
        this.isDone = true;
    }


    /**
     * Marks this task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Retrieves the title or description of this task.
     *
     * @return the task title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the serialized string representation of this task
     * for saving into persistent storage.
     * <p>
     * Subclasses must implement this method to provide their
     * specific save format.
     *
     * @return the save format string for this task
     */
    public abstract String toSaveFormat();
}
