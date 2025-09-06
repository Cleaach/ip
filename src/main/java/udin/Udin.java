package udin;

import java.io.IOException;
import java.util.List;

/**
 * The main entry point and controller class for the Udin task manager application.
 * <p>
 * This class coordinates interactions between the user interface ({@link Ui}),
 * persistent data storage ({@link Storage}), and the in-memory task list ({@link TaskList}).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Initialize the system by loading tasks from disk</li>
 *   <li>Process user input commands</li>
 *   <li>Update and display tasks accordingly</li>
 *   <li>Persist tasks back to storage</li>
 * </ul>
 */
public class Udin {

    /**
     * Help page for new users.
     */
    private static final String HELP =
            "Command list: \n" +
                    "- list: List your tasks.\n" +
                    "- bye: End the conversation with Udin, yours truly.\n" +
                    "- mark <task number>: Mark a task as done.\n" +
                    "- unmark <task number>: Mark a task as not done.\n" +
                    "- todo <description>: Create a new to-do task with a description.\n" +
                    "- deadline <description> <deadline>: Create a new deadline task with a description and a deadline.\n" +
                    "- event <description> <from> <to>: Create a new event task with a start and end time.\n" +
                    "- delete <task number>: Delete a task from the task list.\n" +
                    "- help: Show this command list.\n" +
                    "\n" +
                    "All times (for deadline and event commands) must be\n" +
                    "formatted as yyyy-mm-dd hhmm.";

    /**
     * Handles input/output with the user (e.g., displaying messages, errors, and reading commands).
     */
    private final Ui ui;

    /**
     * Manages reading from and writing to the persistent storage file.
     */
    private final Storage storage;

    /**
     * The in-memory list of tasks that the user manages during runtime.
     */
    private final TaskList tasks;

    /**
     * Constructs a new Udin instance with the given file path for storage.
     * <p>
     * Attempts to load existing tasks from the specified file. If loading fails,
     * initializes with an empty {@link TaskList}.
     *
     * @param filePath the file path where tasks are saved and loaded
     */
    public Udin(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList tmp;
        try {
            tmp = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Failed to load tasks: " + e.getMessage());
            tmp = new TaskList();
        }
        tasks = tmp;

        assert tasks != null : "TaskList should always be initialized";
    }

    /**
     * Generates a response for the user's chat message in JavaFX mode.
     *
     * @param input the user command
     * @return the response string
     */
    public String getResponse(String input) {
        try {
            if (Parser.isBye(input)) {
                try {
                    storage.save(tasks.getAll());
                } catch (IOException e) {
                    return "Error saving tasks: " + e.getMessage();
                }
                return "Bye. Hope to see you again soon!";
            } else if (Parser.isList(input)) {
                return tasks.show();
            } else if (Parser.isMark(input)) {
                int idx = Parser.parseIndex(input);
                assert idx >= 0 && idx < tasks.size() : "Index should be within valid range";
                if (idx < 0 || idx >= tasks.size()) {
                    return "Invalid task number.";
                } else {
                    tasks.mark(idx);
                    return "Good boy! This task is all done:\n" + tasks.get(idx).display();
                }
            } else if (Parser.isUnmark(input)) {
                int idx = Parser.parseIndex(input);
                assert idx >= 0 && idx < tasks.size() : "Index should be within valid range";
                if (idx < 0 || idx >= tasks.size()) {
                    return "Invalid task number.";
                } else {
                    tasks.unmark(idx);
                    return "This task was unmarked:\n" + tasks.get(idx).display();
                }
            } else if (Parser.isTodo(input)) {
                String desc = input.substring(5).trim();
                if (desc.isBlank()) {
                    return "The description of a todo cannot be empty.";
                } else {
                    Task t = new ToDo(desc);
                    tasks.add(t);
                    return "Got it. I've added this task:\n  " + t.display()
                            + "\nNow you have " + tasks.size() + " tasks in the list.";
                }
            } else if (Parser.isDeadline(input)) {
                String[] p = Parser.parseDeadlineParts(input);
                if (p.length < 2 || p[0].isBlank() || p[1].isBlank()) {
                    return "The description or /by date of a deadline cannot be empty.";
                } else {
                    try {
                        Task t = new Deadline(p[0], p[1]);
                        tasks.add(t);
                        return "Got it. I've added this task:\n  " + t.display()
                                + "\nNow you have " + tasks.size() + " tasks in the list.";
                    } catch (Exception e) {
                        return "Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).";
                    }
                }
            } else if (Parser.isEvent(input)) {
                String[] p = Parser.parseEventParts(input);
                if (p.length < 3 || p[0].isBlank() || p[1].isBlank() || p[2].isBlank()) {
                    return "The description or dates of an event cannot be empty.";
                } else {
                    try {
                        Task t = new Event(p[0], p[1], p[2]);
                        tasks.add(t);
                        return "Got it. I've added this task:\n  " + t.display()
                                + "\nNow you have " + tasks.size() + " tasks in the list.";
                    } catch (Exception e) {
                        return "Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).";
                    }
                }
            } else if (Parser.isDelete(input)) {
                try {
                    int idx = Parser.parseIndex(input);
                    if (idx < 0 || idx >= tasks.size()) {
                        return "Invalid task number.";
                    } else {
                        Task removed = tasks.remove(idx);
                        return "Noted. I've removed this task:\n   " +
                                removed.display() + "\nNow you have " + tasks.size() + " tasks in the list.";
                    }
                } catch (NumberFormatException e) {
                    return "Please provide a valid task number to delete.";
                }
            } else if (Parser.isHelp(input)) {
                return Udin.HELP;
            } else {
                return "Unrecognized command: " + input;
            }
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }
}
