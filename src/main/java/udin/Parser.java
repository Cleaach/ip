package udin;


import java.io.IOException;
import java.util.List;


public class Parser {
   public static boolean isList(String cmd) { return "list".equals(cmd); }
   public static boolean isBye(String cmd) { return "bye".equals(cmd); }
   public static boolean isMark(String cmd) { return cmd.startsWith("mark "); }
   public static boolean isUnmark(String cmd) { return cmd.startsWith("unmark "); }
   public static boolean isTodo(String cmd) { return cmd.startsWith("todo "); }
   public static boolean isDeadline(String cmd) { return cmd.startsWith("deadline "); }
   public static boolean isEvent(String cmd) { return cmd.startsWith("event "); }
   public static boolean isDelete(String cmd) { return cmd.startsWith("delete "); }
   public static boolean isHelp(String cmd) {return "help".equals(cmd); }


   public static int parseIndex(String cmd) {
       return Integer.parseInt(cmd.split(" ")[1]) - 1;
   }


   public static String[] parseDeadlineParts(String input) {
       String[] parts = input.substring(9).split("/by", 2);
       return new String[]{ parts[0].trim(), parts.length > 1 ? parts[1].trim() : "" };
   }


   public static String[] parseEventParts(String input) {
       String[] parts = input.substring(6).split("/from|/to");
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
               return handleMarkCommand(command, tasks);
           } else if (isUnmark(command)) {
               return handleUnmarkCommand(command, tasks);
           } else if (isTodo(command)) {
               return handleTodoCommand(command, tasks);
           } else if (isDeadline(command)) {
               return handleDeadlineCommand(command, tasks);
           } else if (isEvent(command)) {
               return handleEventCommand(command, tasks);
           } else if (isDelete(command)) {
               return handleDeleteCommand(command, tasks);
           } else if (isHelp(command)) {
               return Udin.HELP;
           } else if (command.startsWith("find ")) {
               return handleFindCommand(command, tasks);
           } else {
               return "Unrecognized command: " + command;
           }
       } catch (Exception e) {
           return "An unexpected error occurred: " + e.getMessage();
       }
   }


   /**
    * Executes a command with UI feedback for CLI mode.
    *
    * @param command the command to execute
    * @param tasks the task list to operate on
    * @param storage the storage to save tasks
    * @param ui the UI to display messages
    */
   public static void executeCommandWithUi(String command, TaskList tasks, Storage storage, Ui ui) {
       try {
           if (isBye(command)) {
               try {
                   storage.save(tasks.getAll());
               } catch (IOException e) {
                   ui.showError("Error saving tasks: " + e.getMessage());
               }
               ui.showBye();
           } else if (isList(command)) {
               ui.showList(tasks);
           } else if (isMark(command)) {
               handleMarkCommandWithUi(command, tasks, ui);
           } else if (isUnmark(command)) {
               handleUnmarkCommandWithUi(command, tasks, ui);
           } else if (isTodo(command)) {
               handleTodoCommandWithUi(command, tasks, ui);
           } else if (isDeadline(command)) {
               handleDeadlineCommandWithUi(command, tasks, ui);
           } else if (isEvent(command)) {
               handleEventCommandWithUi(command, tasks, ui);
           } else if (isDelete(command)) {
               handleDeleteCommandWithUi(command, tasks, ui);
           } else if (command.startsWith("find ")) {
               handleFindCommandWithUi(command, tasks, ui);
           } else {
               ui.showError("Unrecognized command: " + command);
           }
       } catch (Exception e) {
           ui.showError("An unexpected error occurred: " + e.getMessage());
       }
   }


   private static String handleMarkCommand(String command, TaskList tasks) {
       int idx = parseIndex(command);
       if (idx < 0 || idx >= tasks.size()) {
           return "Invalid task number.";
       } else {
           tasks.mark(idx);
           return "Good boy! This task is all done:\n" + tasks.get(idx).display();
       }
   }


   private static void handleMarkCommandWithUi(String command, TaskList tasks, Ui ui) {
       int idx = parseIndex(command);
       if (idx < 0 || idx >= tasks.size()) {
           ui.showError("Invalid task number.");
       } else {
           tasks.mark(idx);
           ui.showMessage("Good boy! This task is all done:\n" + tasks.get(idx).display());
       }
   }


   private static String handleUnmarkCommand(String command, TaskList tasks) {
       int idx = parseIndex(command);
       if (idx < 0 || idx >= tasks.size()) {
           return "Invalid task number.";
       } else {
           tasks.unmark(idx);
           return "This task was unmarked:\n" + tasks.get(idx).display();
       }
   }


   private static void handleUnmarkCommandWithUi(String command, TaskList tasks, Ui ui) {
       int idx = parseIndex(command);
       if (idx < 0 || idx >= tasks.size()) {
           ui.showError("Invalid task number.");
       } else {
           tasks.unmark(idx);
           ui.showMessage("This task was unmarked:\n" + tasks.get(idx).display());
       }
   }


   private static String handleTodoCommand(String command, TaskList tasks) {
       String desc = command.substring(5).trim();
       if (desc.isBlank()) {
           return "The description of a todo cannot be empty.";
       } else {
           Task t = new ToDo(desc);
           tasks.add(t);
           return "Got it. I've added this task:\n  " + t.display()
                   + "\nNow you have " + tasks.size() + " tasks in the list.";
       }
   }


   private static void handleTodoCommandWithUi(String command, TaskList tasks, Ui ui) {
       String desc = command.substring(5).trim();
       if (desc.isBlank()) {
           ui.showError("The description of a todo cannot be empty.");
       } else {
           Task t = new ToDo(desc);
           tasks.add(t);
           ui.showAddMessage(t, tasks.size());
       }
   }


   private static String handleDeadlineCommand(String command, TaskList tasks) {
       String[] p = parseDeadlineParts(command);
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
   }


   private static void handleDeadlineCommandWithUi(String command, TaskList tasks, Ui ui) {
       String[] p = parseDeadlineParts(command);
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
   }


   private static String handleEventCommand(String command, TaskList tasks) {
       String[] p = parseEventParts(command);
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
   }


   private static void handleEventCommandWithUi(String command, TaskList tasks, Ui ui) {
       String[] p = parseEventParts(command);
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
   }


   private static String handleDeleteCommand(String command, TaskList tasks) {
       try {
           int idx = parseIndex(command);
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
   }


   private static void handleDeleteCommandWithUi(String command, TaskList tasks, Ui ui) {
       try {
           int idx = parseIndex(command);
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
   }


   private static String handleFindCommand(String command, TaskList tasks) {
       String keyword = command.substring(5).trim();
       if (keyword.isBlank()) {
           return "Please provide a keyword to find.";
       } else {
           List<Task> foundTasks = tasks.findTasksByKeyword(keyword);
           return formatFoundTasks(foundTasks);
       }
   }


   private static void handleFindCommandWithUi(String command, TaskList tasks, Ui ui) {
       String keyword = command.substring(5).trim();
       if (keyword.isBlank()) {
           ui.showError("Please provide a keyword to find.");
       } else {
           List<Task> foundTasks = tasks.findTasksByKeyword(keyword);
           ui.showFoundTasks(foundTasks);
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