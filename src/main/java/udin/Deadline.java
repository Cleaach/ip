package udin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for a task with a single deadline
 * @author Clement Chendra
 * @version 0.1
 * @since 0.1
 */
public class Deadline extends Task{
    private LocalDateTime deadline;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructor for Deadline class
     *
     * @param title a string to describe the task
     * @param deadline a string for the deadline in yyyy-MM-dd HHmm format
     * @throws DateTimeParseException if the deadline format is invalid
     */
    public Deadline(String title, String deadline) {
        super(title);
        this.deadline = LocalDateTime.parse(deadline, INPUT_FORMAT);
    }

    /**
     * Formats the task for Udin's list method
     *
     * @return a string correctly formatted for list
     */
    @Override
    public String display() {
        return "[D]" + super.display() + " (by: " + this.deadline.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Formats the task to save locally in data/tasks.txt
     *
     * @return a string correctly formatted for storage
     */
    @Override
    public String toSaveFormat() {
        return "D," + (isDone ? "1" : "0") + "," + title + "," + deadline.format(INPUT_FORMAT);
    }
}
