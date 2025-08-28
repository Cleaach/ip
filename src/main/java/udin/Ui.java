package udin;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner sc = new Scanner(System.in);

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm duke.Udin!\n Let me load up your saved tasks...");
    }

    public void showLoadSuccess() {
        System.out.println(" Load successful. What can I help you with?");
        System.out.println(LINE);
    }

    public String readCommand() {
        if (sc.hasNextLine()) return sc.nextLine();
        return null;
    }

    public void showAddMessage(Task t, int total) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:\n   " + t.display());
        System.out.println(" Now you have " + total + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("\n Your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).display());
        }
        System.out.println(LINE);
    }

    public void showMessage(String s) {
        System.out.println(LINE);
        System.out.println(s);
        System.out.println(LINE);
    }

    public void showError(String s) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + s);
        System.out.println(LINE);
    }

    public void showBye() {
        System.out.println(LINE);
        System.out.println(" Bye!");
        System.out.println(LINE);
    }

    /**
     * Displays a list of matching tasks found by a search.
     *
     * @param foundTasks the list of tasks matching the search criteria
     */
    public void showFoundTasks(List<Task> foundTasks) {
        System.out.println(LINE);
        if (foundTasks.isEmpty()) {
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < foundTasks.size(); i++) {
                System.out.println("  " + (i + 1) + "." + foundTasks.get(i).display());
            }
        }
        System.out.println(LINE);
    }

}
