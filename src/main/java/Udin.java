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
        } else {
            Udin.add(command);
            Udin.scan();
        }

    }

    private static void echo(String command) {
        System.out.println(Udin.line + " " + command + "\n" + Udin.line);
    }

    private static void bye() {
        System.out.println(Udin.line + " Bye!\n" + Udin.line);
    }

    private static void add(String line) {
        Udin.todo.add(new Task(line));
        System.out.println(Udin.line + " added: " + line + "\n" + Udin.line);
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
        System.out.println(Udin.line + "Good boy! This task is all done:\n" + Udin.todo.get(index).display());
    }

    private static void unmark(int index) {
        Udin.todo.get(index).unmark();
        System.out.println(Udin.line + "This task was unmarked:\n" + Udin.todo.get(index).display());
    }
}
