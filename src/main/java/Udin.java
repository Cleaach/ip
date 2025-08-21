import java.util.ArrayList;
import java.util.Scanner;

public class Udin {
    private static final String line = "____________________________________________________________\n";
    private static ArrayList<Task> todo = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(Udin.line +
                " Hello! I'm Udin!\n" +
                " What can I do for you?\n" +
                Udin.line);
        Udin.scan();
    }

    private static void scan() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals("bye")) {
            Udin.bye();
        } else if (command.equals("list")) {
            Udin.list();
            Udin.scan();
        } else if (command.startsWith("mark ")) {
            Udin.mark(Integer.parseInt(command.split(" ")[1]) - 1);
            Udin.scan();
        } else if (command.startsWith("unmark ")) {
            Udin.unmark(Integer.parseInt(command.split(" ")[1]) - 1);
            Udin.scan();
        } else if (command.startsWith("todo ")) {
            Udin.addToDo(command.substring(5));
            Udin.scan();
        } else if (command.startsWith("deadline ")) {
            Udin.addDeadline(command);
            Udin.scan();
        } else if (command.startsWith("event ")) {
            Udin.addEvent(command);
            Udin.scan();
        } else {
            Udin.misc(command);
            Udin.scan();
        }

    }

    private static void misc(String command) {
        System.out.println(Udin.line + " Unrecognized command: " + command + "\n" + Udin.line);
    }

    private static void bye() {
        System.out.println(Udin.line + " Bye!\n" + Udin.line);
    }

    private static void addToDo(String desc) {
        Task t = new ToDo(desc);
        Udin.todo.add(t);
        printAddMessage(t);
    }

    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split("/by", 2);
        String desc = parts[0].trim();
        String by = parts[1].trim();
        Task t = new Deadline(desc, by);
        Udin.todo.add(t);
        printAddMessage(t);
    }

    private static void addEvent(String input) {
        String[] parts = input.substring(6).split("/from|/to");
        String desc = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        Task t = new Event(desc, from, to);
        Udin.todo.add(t);
        printAddMessage(t);
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
        Udin.todo.get(index).mark();
        System.out.println(Udin.line + "Good boy! This task is all done:\n" + Udin.todo.get(index).display() + "\n" + Udin.line);
    }

    private static void unmark(int index) {
        Udin.todo.get(index).unmark();
        System.out.println(Udin.line + "This task was unmarked:\n" + Udin.todo.get(index).display() + "\n" + Udin.line);
    }
}
