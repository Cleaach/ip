package udin;

import java.io.IOException;
import java.util.List;

public class Parser {
    // Command prefix lengths
    private static final int TODO_PREFIX_LENGTH = 5;
    private static final int DEADLINE_PREFIX_LENGTH = 9;
    private static final int EVENT_PREFIX_LENGTH = 6;
    private static final int FIND_PREFIX_LENGTH = 5;
    
    // Array indices and validation
    private static final int COMMAND_INDEX = 1;
    private static final int DEADLINE_MIN_PARTS = 2;
    private static final int EVENT_MIN_PARTS = 3;
    
    public static boolean isList(String cmd) { return "list".equals(cmd); }
    public static boolean isBye(String cmd) { return "bye".equals(cmd); }
    public static boolean isMark(String cmd) { return cmd.startsWith("mark "); }
    public static boolean isUnmark(String cmd) { return cmd.startsWith("unmark "); }
    public static boolean isTodo(String cmd) { return cmd.startsWith("todo "); }
    public static boolean isDeadline(String cmd) { return cmd.startsWith("deadline "); }
    public static boolean isEvent(String cmd) { return cmd.startsWith("event "); }
    public static boolean isDelete(String cmd) { return cmd.startsWith("delete "); }
    public static boolean isHelp(String cmd) { return "help".equals(cmd); }

    public static int parseIndex(String cmd) {
        String[] parts = cmd.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid command format. Expected: command <number>");
        }
        try {
            int index = Integer.parseInt(parts[COMMAND_INDEX]);
            if (index < 1) {
                throw new IllegalArgumentException("Task number must be positive");
            }
            return index - 1;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Task number must be a valid integer");
        }
    }

    public static String[] parseDeadlineParts(String input) {
        String[] parts = input.substring(DEADLINE_PREFIX_LENGTH).split("/by", 2);
        return new String[]{ parts[0].trim(), parts.length > 1 ? parts[1].trim() : "" };
    }

    public static String[] parseEventParts(String input) {
        String[] parts = input.substring(EVENT_PREFIX_LENGTH).split("/from|/to");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();
        return parts;
    }


    /**
     * Executes a command and returns the result for JavaFX mode.
     *
     * @param command the command to execute
     * @param tasks the task list to operate on
     * @param storage the storage to save tasks
     * @return the response string
     */
    public static String executeCommand(String command, TaskList tasks, Storage storage) {
        try {
            if (isBye(command)) {
                try {
                    storage.save(tasks.getAll());
                } catch (IOException e) {
                    return "Error saving tasks: " + e.getMessage();
                }
                return "Bye. Hope to see you again soon!";
            } else if (isList(command)) {
                return tasks.show();
            } else if (isMark(command)) {
                return handleMarkCommand(command, tasks, storage);
            } else if (isUnmark(command)) {
                return handleUnmarkCommand(command, tasks, storage);
            } else if (command.trim().equals("todo")) {
                return "The description of a todo cannot be empty.";
            } else if (isTodo(command)) {
                return handleTodoCommand(command, tasks, storage);
            } else if (isDeadline(command)) {
                return handleDeadlineCommand(command, tasks, storage);
            } else if (isEvent(command)) {
                return handleEventCommand(command, tasks, storage);
            } else if (isDelete(command)) {
                return handleDeleteCommand(command, tasks, storage);
            } else if (isHelp(command)) {
                return Udin.HELP;
            } else if (command.startsWith("find ")) {
                return handleFindCommand(command, tasks);
            } else if (command.trim().equals("find")) {
                return "Please provide a keyword to find.";
            } else {
                return "Unrecognized command: " + command + ". Type 'help' to see the list of commands.";
            }
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }


    private static String handleMarkCommand(String command, TaskList tasks, Storage storage) {
        int idx = parseIndex(command);
        if (idx < 0 || idx >= tasks.size()) {
            return "Invalid task number.";
        } else {
            tasks.mark(idx);
            try {
                storage.save(tasks.getAll());
            } catch (IOException e) {
                return "Good boy! This task is all done:\n" + tasks.get(idx).display()
                        + "\nWarning: Failed to save tasks: " + e.getMessage();
            }
            return "Good boy! This task is all done:\n" + tasks.get(idx).display();
        }
    }

    private static String handleUnmarkCommand(String command, TaskList tasks, Storage storage) {
        int idx = parseIndex(command);
        if (idx < 0 || idx >= tasks.size()) {
            return "Invalid task number.";
        } else {
            tasks.unmark(idx);
            try {
                storage.save(tasks.getAll());
            } catch (IOException e) {
                return "This task was unmarked:\n" + tasks.get(idx).display()
                        + "\nWarning: Failed to save tasks: " + e.getMessage();
            }
            return "This task was unmarked:\n" + tasks.get(idx).display();
        }
    }

    private static String handleTodoCommand(String command, TaskList tasks, Storage storage) {
        String desc = command.substring(TODO_PREFIX_LENGTH).trim();
        if (desc.isBlank()) {
            return "The description of a todo cannot be empty.";
        } else {
            Task t = new ToDo(desc);
            tasks.add(t);
            try {
                storage.save(tasks.getAll());
            } catch (IOException e) {
                return "Got it. I've added this task:\n  " + t.display()
                        + "\nNow you have " + tasks.size() + " tasks in the list.\n"
                        + "Warning: Failed to save tasks: " + e.getMessage();
            }
            return "Got it. I've added this task:\n  " + t.display()
                    + "\nNow you have " + tasks.size() + " tasks in the list.";
        }
    }

    private static String handleDeadlineCommand(String command, TaskList tasks, Storage storage) {
        String[] p = parseDeadlineParts(command);
        if (p.length < DEADLINE_MIN_PARTS || p[0].isBlank() || p[1].isBlank()) {
            return "The description or /by date of a deadline cannot be empty.";
        } else {
            try {
                Task t = new Deadline(p[0], p[1]);
                tasks.add(t);
                try {
                    storage.save(tasks.getAll());
                } catch (IOException e) {
                    return "Got it. I've added this task:\n  " + t.display()
                            + "\nNow you have " + tasks.size() + " tasks in the list.\n"
                            + "Warning: Failed to save tasks: " + e.getMessage();
                }
                return "Got it. I've added this task:\n  " + t.display()
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            } catch (Exception e) {
                return "Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).";
            }
        }
    }

    private static String handleEventCommand(String command, TaskList tasks, Storage storage) {
        String[] p = parseEventParts(command);
        if (p.length < EVENT_MIN_PARTS || p[0].isBlank() || p[1].isBlank() || p[2].isBlank()) {
            return "The description or dates of an event cannot be empty.";
        } else {
            try {
                Task t = new Event(p[0], p[1], p[2]);
                tasks.add(t);
                try {
                    storage.save(tasks.getAll());
                } catch (IOException e) {
                    return "Got it. I've added this task:\n  " + t.display()
                            + "\nNow you have " + tasks.size() + " tasks in the list.\n"
                            + "Warning: Failed to save tasks: " + e.getMessage();
                }
                return "Got it. I've added this task:\n  " + t.display()
                        + "\nNow you have " + tasks.size() + " tasks in the list.";
            } catch (Exception e) {
                return "Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).";
            }
        }
    }

    private static String handleDeleteCommand(String command, TaskList tasks, Storage storage) {
        try {
            int idx = parseIndex(command);
            if (idx < 0 || idx >= tasks.size()) {
                return "Invalid task number.";
            } else {
                Task removed = tasks.remove(idx);
                try {
                    storage.save(tasks.getAll());
                } catch (IOException e) {
                    return "Noted. I've removed this task:\n   " +
                            removed.display() + "\nNow you have " + tasks.size() + " tasks in the list.\n"
                            + "Warning: Failed to save tasks: " + e.getMessage();
                }
                return "Noted. I've removed this task:\n   " +
                        removed.display() + "\nNow you have " + tasks.size() + " tasks in the list.";
            }
        } catch (NumberFormatException e) {
            return "Please provide a valid task number to delete.";
        }
    }

    private static String handleFindCommand(String command, TaskList tasks) {
        String keyword = command.substring(FIND_PREFIX_LENGTH).trim();
        if (keyword.isBlank()) {
            return "Please provide a keyword to find.";
        } else {
            List<Task> foundTasks = tasks.findTasksByKeyword(keyword);
            return formatFoundTasks(foundTasks);
        }
    }

    private static String formatFoundTasks(List<Task> foundTasks) {
        if (foundTasks.isEmpty()) {
            return "No tasks found matching your search.";
        }
        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < foundTasks.size(); i++) {
            result.append((i + 1)).append(".").append(foundTasks.get(i).display()).append("\n");
        }
        return result.toString().trim();
    }
}
