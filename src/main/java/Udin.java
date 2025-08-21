import java.util.Scanner;

public class Udin {
    private static final String line = "____________________________________________________________\n";

    public static void main(String[] args) {
        System.out.println(Udin.line +
                " Hello! I'm Udin!\n" +
                " What can I do for you?\n" +
                Udin.line);
        Udin.read();
    }

    private static void read() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals("bye")) {
            Udin.bye();
        } else {
            Udin.echo(command);
            Udin.read();
        }

    }

    private static void echo(String command) {
        System.out.println(Udin.line + " " + command + "\n" + Udin.line);
    }

    private static void bye() {
        System.out.println(Udin.line + " Bye!\n" + Udin.line);
    }

}
