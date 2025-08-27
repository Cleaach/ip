import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Udin {
    private static final String line = "____________________________________________________________\n";
    private static ArrayList<Task> todo = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(Udin.line + " Hello! I'm Udin!\n Let me load up your saved tasks...");
        SavedTasksFile f = new SavedTasksFile("data/tasks.txt");
        if (!f.exists()) {
            System.out.println(" Error: saved tasks file not present! Exiting...\n" + Udin.line);
            return;
        }

        try {
            loadTasksFromFile(f);
        } catch (Exception e) {
            System.out.println(" Load unsuccessful: " + e.getMessage());
            System.out.println(Udin.line);
            return;
        }

        System.out.println(" Load successful. What can I help you with?\n" + Udin.line);
        Udin.scan();
    }

    private static void loadTasksFromFile(SavedTasksFile f) {
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                String type = parts[0];
                boolean isDone = parts[1].equals("1");

                Task t = null;
                switch (type) {
                    case "T":
                        t = new ToDo(parts[2]);
                        break;
                    case "D":
                        t = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        t = new Event(parts[2], parts[3], parts[4]);
                        break;
                }

                if (t != null) {
                    if (isDone) t.mark();
                    todo.add(t);
                }
            }
        } catch (Exception e) {
            System.out.println(line + " Error while loading tasks: " + e.getMessage() + "\n" + line);
        }
    }


    private static void scan() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {  // avoid crash when input ends
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                Udin.bye();
                break;
            } else if (command.equals("list")) {
                Udin.list();
            } else if (command.startsWith("mark ")) {
                Udin.mark(Integer.parseInt(command.split(" ")[1]) - 1);
            } else if (command.startsWith("unmark ")) {
                Udin.unmark(Integer.parseInt(command.split(" ")[1]) - 1);
            } else if (command.startsWith("todo ")) {
                Udin.addToDo(command.substring(5));
            } else if (command.startsWith("deadline ")) {
                Udin.addDeadline(command);
            } else if (command.startsWith("event ")) {
                Udin.addEvent(command);
            } else if (command.startsWith("delete ")) {
                try {
                    int index = Integer.parseInt(command.split(" ")[1]) - 1;
                    Udin.deleteTask(index);
                } catch (NumberFormatException e) {
                    System.out.println(line + " OOPS!!! Please provide a valid task number to delete.\n" + line);
                }
            } else {
                Udin.misc(command);
            }
        }

        scanner.close();
    }

    private static void misc(String command) {
        System.out.println(Udin.line + " Unrecognized command: " + command + "\n" + Udin.line);
    }

    private static void bye() {
        Udin.saveTasksToFile();
        System.out.println(Udin.line + " Bye!\n" + Udin.line);
    }

    private static void addToDo(String desc) {
        if (desc.isBlank()) {
            System.out.println(line + " OOPS!!! The description of a todo cannot be empty.\n" + line);
            return;
        }
        Task t = new ToDo(desc);
        todo.add(t);
        printAddMessage(t);
    }

    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split("/by", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            System.out.println(line + " OOPS!!! The description or /by date of a deadline cannot be empty.\n" + line);
            return;
        }
        String desc = parts[0].trim();
        String by = parts[1].trim(); // must be yyyy-MM-dd HHmm
        try {
            Task t = new Deadline(desc, by);
            todo.add(t);
            printAddMessage(t);
        } catch (Exception e) {
            System.out.println(line + " OOPS!!! Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).\n" + line);
        }
    }


    private static void addEvent(String input) {
        String[] parts = input.substring(6).split("/from|/to");
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            System.out.println(line + " OOPS!!! The description or dates of an event cannot be empty.\n" + line);
            return;
        }
        String desc = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        try {
            Task t = new Event(desc, from, to);
            todo.add(t);
            printAddMessage(t);
        } catch (Exception e) {
            System.out.println(line + " OOPS!!! Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).\n" + line);
        }
    }


    private static void printAddMessage(Task t) {
        System.out.println(line +
                " Got it. I've added this task:\n   " +
                t.display() +
                "\n Now you have " + Udin.todo.size() + " tasks in the list.\n" +
                line);
    }


    private static void list() {
        int size = Udin.todo.size();
        System.out.println(Udin.line + "\n Your tasks:");
        for (int i = 0; i < size; i++) {
            int number = i + 1;
            System.out.println(" " + number + "." + Udin.todo.get(i).display());
        }
        System.out.println(Udin.line);
    }

    private static void mark(int index) {
        if (index < 0 || index >= todo.size()) {
            System.out.println(line + " OOPS!!! Invalid task number.\n" + line);
            return;
        }
        todo.get(index).mark();
        System.out.println(line + "Good boy! This task is all done:\n" + todo.get(index).display() + "\n" + line);
    }

    private static void unmark(int index) {
        if (index < 0 || index >= todo.size()) {
            System.out.println(line + " OOPS!!! Invalid task number.\n" + line);
            return;
        }
        todo.get(index).unmark();
        System.out.println(line + "This task was unmarked:\n" + todo.get(index).display() + "\n" + line);
    }

    private static void deleteTask(int index) {
        if (index < 0 || index >= todo.size()) {
            System.out.println(line + " OOPS!!! Invalid task number.\n" + line);
            return;
        }

        Task removed = todo.remove(index);
        System.out.println(line +
                " Noted. I've removed this task:\n   " +
                removed.display() +
                "\n Now you have " + todo.size() + " tasks in the list.\n" +
                line);
    }

    public static void saveTasksToFile() {
        try (FileWriter fw = new FileWriter("data/tasks.txt")) {
            for (Task t : todo) {
                fw.write(t.toSaveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }


}
