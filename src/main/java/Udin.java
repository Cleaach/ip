import java.util.ArrayList;
import java.util.Scanner;

public class Udin {
    private static final String line = "____________________________________________________________\n";
    private static ArrayList<String> text = new ArrayList<>();

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
        Udin.text.add(line);
        System.out.println(Udin.line + " added: " + line + "\n" + Udin.line);
    }

    private static void list() {
        int size = Udin.text.size();
        System.out.println(Udin.line);
        for (int i = 0; i < size; i++) {
            int number = i + 1;
            System.out.println(number + ". " + Udin.text.get(i));
        }
        System.out.println(Udin.line);
    }
}
