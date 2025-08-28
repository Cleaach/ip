package udin;

import java.util.Scanner;

/**
 * Handles all interactions with the user via the console.
 * <p>
 * This class is responsible for displaying messages, prompts,
 * and errors, as well as reading commands from the user.
 */
public class Ui {
    /**
     * A visual separator line used in printed messages.
     */
    private static final String LINE = "____________________________________________________________";

    /**
     * Scanner used for reading user input from standard input.
     */
    private final Scanner sc = new Scanner(System.in);

    /**
     * Displays the welcome message when the program starts.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm Udin!\n Let me load up your saved tasks...");
    }

    /**
     * Displays a message indicating tasks were loaded successfully.
     */
    public void showLoadSuccess() {
        System.out.println(" Load successful. What can I help you with?");
        System.out.println(LINE);
    }

    /**
     * Reads a single line of user input from the console.
     *
     * @return the command entered by the user, or {@code null} if no input is available
     */
    public String readCommand() {
        if (sc.hasNextLine()) return sc.nextLine();
        return null;
    }

    /**
     * Displays a message confirming a task has been added,
     * along with the current total number of tasks.
     *
     * @param t the task that was added
     * @param total the total number of tasks currently in the list
     */
    public void showAddMessage(Task t, int total) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:\n   " + t.display());
        System.out.println(" Now you have " + total + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays the full list of tasks.
     *
     * @param tasks the list of tasks to display
     */
    public void showList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("\n Your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i).display());
        }
        System.out.println(LINE);
    }

    /**
     * Displays a general message to the user.
     *
     * @param s the message to display
     */
    public void showMessage(String s) {
        System.out.println(LINE);
        System.out.println(s);
        System.out.println(LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param s the error message to display
     */
    public void showError(String s) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + s);
        System.out.println(LINE);
    }

    /**
     * Displays a goodbye message when the program is exiting.
     */
    public void showBye() {
        System.out.println(LINE);
        System.out.println(" Bye!");
        System.out.println(LINE);
    }
}
