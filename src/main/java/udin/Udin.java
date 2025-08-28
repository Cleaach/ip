package udin;

import java.io.IOException;
import java.util.List;

public class Udin {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

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
                } else if (command.startsWith("find ")) {
                    String keyword = command.substring(5).trim();
                    if (keyword.isBlank()) {
                        ui.showError("Please provide a keyword to find.");
                    } else {
                        List<Task> foundTasks = tasks.findTasksByKeyword(keyword);
                        ui.showFoundTasks(foundTasks);
                    }
                } else {
                    ui.showError("Unrecognized command: " + command);
                }
            } catch (Exception e) {
                ui.showError("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Udin("data/tasks.txt").run();
    }
}
