package udin;

/**
 * Represents a simple "ToDo" task.
 * <p>
 * A ToDo task only has a title and completion status.
 * It does not include additional attributes such as deadlines or event timings.
 */
public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the given title.
     * The task is initially marked as not done.
     *
     * @param title the description of the ToDo task
     */
    public ToDo(String title) {
        super(title);
    }

    /**
     * Returns the display string for this ToDo task.
     * <p>
     * The format is {@code [T][X] title} if the task is done,
     * or {@code [T][ ] title} if not done.
     *
     * @return the formatted display string for this ToDo task
     */
    @Override
    public String display() {
        return "[T]" + super.display();
    }

    /**
     * Returns the serialized string representation of this ToDo task
     * for saving to persistent storage.
     * <p>
     * Format: {@code T,doneFlag,title}
     * <ul>
     *   <li>{@code doneFlag} is "1" if the task is done, "0" if not</li>
     *   <li>{@code title} is the description of the task</li>
     * </ul>
     *
     * @return the string representation in save format
     */
    @Override
    public String toSaveFormat() {
        return "T," + (this.isDone ? "1" : "0") + "," + this.title;
    }
}
