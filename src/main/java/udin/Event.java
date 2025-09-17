package udin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class for a task with a start time and end time
 * @author Clement Chendra
 * @version 0.1
 * @since 0.1
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructor for Event class
     *
     * @param desc a string to describe the task
     * @param from a string for the start time in yyyy-MM-dd HHmm format
     * @param to a string for the end time in yyyy-MM-dd HHmm format
     * @throws DateTimeParseException if the date format is invalid
     */
    public Event(String desc, String from, String to) {
        super(desc);
        this.from = LocalDateTime.parse(from, INPUT_FORMAT);
        this.to = LocalDateTime.parse(to, INPUT_FORMAT);
    }

    /**
     * Formats the task for Udin's list method
     *
     * @return a string correctly formatted for list
     */
    @Override
    public String display() {
        return "[E]" + super.display() +
                " (from: " + from.format(OUTPUT_FORMAT) +
                " to: " + to.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Formats the task to save locally in data/tasks.txt
     *
     * @return a string correctly formatted for storage
     */
    @Override
    public String toSaveFormat() {
        return "E," + (isDone ? "1" : "0") + "," + title + "," + from.format(INPUT_FORMAT) + "," + to.format(INPUT_FORMAT);
    }
}
