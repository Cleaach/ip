package udin;

import java.io.IOException;

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
        } catch (IOException e) {
            ui.showError("Failed to load tasks: " + e.getMessage());
            tmp = new TaskList();
        }
        tasks = tmp;
    }

    /**
     * Runs the interactive command loop for the Udin application.
     * <p>
     * The loop continues until the user issues a "bye" command.
     * Within the loop, commands are parsed by {@link Parser} and handled accordingly:
     * <ul>
     *   <li>Show task list</li>
     *   <li>Mark/unmark tasks</li>
     *   <li>Add ToDo, Deadline, or Event tasks</li>
     *   <li>Delete tasks</li>
     *   <li>Exit and save tasks</li>
     * </ul>
     * Input errors and unexpected exceptions are caught and displayed to the user.
     */
    public void run() {
        ui.showWelcome();
        ui.showLoadSuccess();

        while (true) {
            String command = ui.readCommand();
            if (command == null) break;

            try {
                if (Parser.isBye(command)) {
                    // save then exit
                    try {
                        storage.save(tasks.getAll());
                    } catch (IOException e) {
                        ui.showError("Error saving tasks: " + e.getMessage());
                    }
                    ui.showBye();
                    break;
                } else if (Parser.isList(command)) {
                    ui.showList(tasks);
                } else if (Parser.isMark(command)) {
                    int idx = Parser.parseIndex(command);
                    if (idx < 0 || idx >= tasks.size()) {
                        ui.showError("Invalid task number.");
                    } else {
                        tasks.mark(idx);
                        ui.showMessage("Good boy! This task is all done:\n" + tasks.get(idx).display());
                    }
                } else if (Parser.isUnmark(command)) {
                    int idx = Parser.parseIndex(command);
                    if (idx < 0 || idx >= tasks.size()) {
                        ui.showError("Invalid task number.");
                    } else {
                        tasks.unmark(idx);
                        ui.showMessage("This task was unmarked:\n" + tasks.get(idx).display());
                    }
                } else if (Parser.isTodo(command)) {
                    String desc = command.substring(5).trim();
                    if (desc.isBlank()) {
                        ui.showError("The description of a todo cannot be empty.");
                    } else {
                        Task t = new ToDo(desc);
                        tasks.add(t);
                        ui.showAddMessage(t, tasks.size());
                    }
                } else if (Parser.isDeadline(command)) {
                    String[] p = Parser.parseDeadlineParts(command);
                    if (p.length < 2 || p[0].isBlank() || p[1].isBlank()) {
                        ui.showError("The description or /by date of a deadline cannot be empty.");
                    } else {
                        try {
                            Task t = new Deadline(p[0], p[1]);
                            tasks.add(t);
                            ui.showAddMessage(t, tasks.size());
                        } catch (Exception e) {
                            ui.showError("Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).");
                        }
                    }
                } else if (Parser.isEvent(command)) {
                    String[] p = Parser.parseEventParts(command);
                    if (p.length < 3 || p[0].isBlank() || p[1].isBlank() || p[2].isBlank()) {
                        ui.showError("The description or dates of an event cannot be empty.");
                    } else {
                        try {
                            Task t = new Event(p[0], p[1], p[2]);
                            tasks.add(t);
                            ui.showAddMessage(t, tasks.size());
                        } catch (Exception e) {
                            ui.showError("Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).");
                        }
                    }
                } else if (Parser.isDelete(command)) {
                    try {
                        int idx = Parser.parseIndex(command);
                        if (idx < 0 || idx >= tasks.size()) {
                            ui.showError("Invalid task number.");
                        } else {
                            Task removed = tasks.remove(idx);
                            ui.showMessage("Noted. I've removed this task:\n   " +
                                    removed.display() + "\n Now you have " + tasks.size() + " tasks in the list.");
                        }
                    } catch (NumberFormatException e) {
                        ui.showError("Please provide a valid task number to delete.");
                    }
                } else {
                    ui.showError("Unrecognized command: " + command);
                }
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * The main entry point of the application.
     * <p>
     * Creates a new {@code Udin} instance with the default data file path
     * ({@code data/tasks.txt}) and starts the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Udin("data/tasks.txt").run();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Udin heard: " + input;
    }
}
