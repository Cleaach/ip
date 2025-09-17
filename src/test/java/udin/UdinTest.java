package udin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class UdinTest {
    private Udin udin;
    private String testFilePath = "test_udin_tasks.txt";
    private File testFile;

    @BeforeEach
    public void setUp() {
        udin = new Udin(testFilePath);
        testFile = new File(testFilePath);
    }

    @AfterEach
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testUdinConstructorWithNonExistentFile() {
        Udin newUdin = new Udin("nonexistent_file.txt");
        assertNotNull(newUdin);
        // Should initialize with empty task list
        assertEquals(0, newUdin.getResponse("list").split("\n").length - 2); // -2 for header and footer
    }

    @Test
    public void testGetResponseListEmpty() {
        String response = udin.getResponse("list");
        assertTrue(response.contains("Your tasks:"));
        assertFalse(response.contains("1.")); // No tasks should be listed
    }

    @Test
    public void testGetResponseListWithTasks() {
        udin.getResponse("todo test task");
        String response = udin.getResponse("list");
        assertTrue(response.contains("Your tasks:"));
        assertTrue(response.contains("1."));
        assertTrue(response.contains("test task"));
    }

    @Test
    public void testGetResponseTodo() {
        String response = udin.getResponse("todo test task");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("test task"));
        assertTrue(response.contains("Now you have 1 tasks in the list"));
    }

    @Test
    public void testGetResponseTodoEmpty() {
        String response = udin.getResponse("todo");
        assertEquals("The description of a todo cannot be empty.", response);
    }

    @Test
    public void testGetResponseDeadline() {
        String response = udin.getResponse("deadline test /by 2024-12-25 1200");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("test"));
        assertTrue(response.contains("Now you have 1 tasks in the list"));
    }

    @Test
    public void testGetResponseEvent() {
        String response = udin.getResponse("event test /from 2024-12-25 1200 /to 2024-12-25 1400");
        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("test"));
        assertTrue(response.contains("Now you have 1 tasks in the list"));
    }

    @Test
    public void testGetResponseMark() {
        udin.getResponse("todo test task");
        String response = udin.getResponse("mark 1");
        assertTrue(response.contains("Good boy! This task is all done:"));
    }

    @Test
    public void testGetResponseMarkInvalidIndex() {
        String response = udin.getResponse("mark 1");
        assertEquals("Invalid task number.", response);
    }

    @Test
    public void testGetResponseUnmark() {
        udin.getResponse("todo test task");
        udin.getResponse("mark 1");
        String response = udin.getResponse("unmark 1");
        assertTrue(response.contains("This task was unmarked:"));
    }

    @Test
    public void testGetResponseDelete() {
        udin.getResponse("todo test task");
        String response = udin.getResponse("delete 1");
        assertTrue(response.contains("Noted. I've removed this task:"));
        assertTrue(response.contains("Now you have 0 tasks in the list"));
    }

    @Test
    public void testGetResponseFind() {
        udin.getResponse("todo buy groceries");
        udin.getResponse("todo study for exam");
        udin.getResponse("todo buy milk");
        
        String response = udin.getResponse("find buy");
        assertTrue(response.contains("Here are the matching tasks in your list:"));
        assertTrue(response.contains("buy groceries"));
        assertTrue(response.contains("buy milk"));
        assertFalse(response.contains("study for exam"));
    }

    @Test
    public void testGetResponseFindEmpty() {
        String response = udin.getResponse("find");
        assertEquals("Please provide a keyword to find.", response);
    }

    @Test
    public void testGetResponseHelp() {
        String response = udin.getResponse("help");
        assertEquals(Udin.HELP, response);
    }

    @Test
    public void testGetResponseBye() {
        String response = udin.getResponse("bye");
        assertEquals("Bye. Hope to see you again soon!", response);
    }

    @Test
    public void testGetResponseUnrecognizedCommand() {
        String response = udin.getResponse("unknown command");
        assertEquals("Unrecognized command: unknown command. Type 'help' to see the list of commands.", response);
    }

    @Test
    public void testGetResponseInvalidDateFormat() {
        String response = udin.getResponse("deadline test /by invalid-date");
        assertEquals("Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).", response);
    }

    @Test
    public void testGetResponseInvalidEventDateFormat() {
        String response = udin.getResponse("event test /from 2024-13-45 2500 /to 2024-12-25 1400");
        assertEquals("Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).", response);
    }

    @Test
    public void testGetResponseInvalidTaskNumber() {
        String response = udin.getResponse("mark abc");
        assertEquals("An unexpected error occurred: Task number must be a valid integer", response);
    }

    @Test
    public void testGetResponseInsufficientCommandParts() {
        String response = udin.getResponse("mark");
        assertEquals("Unrecognized command: mark. Type 'help' to see the list of commands.", response);
    }

    @Test
    public void testGetResponseNegativeTaskNumber() {
        String response = udin.getResponse("mark 0");
        assertEquals("An unexpected error occurred: Task number must be positive", response);
    }

    @Test
    public void testTaskPersistence() {
        // Add a task
        udin.getResponse("todo test task");
        udin.getResponse("bye"); // This should save the task
        
        // Create new Udin instance to test loading
        Udin newUdin = new Udin(testFilePath);
        String response = newUdin.getResponse("list");
        assertTrue(response.contains("test task"));
    }

    @Test
    public void testMultipleTasksPersistence() {
        // Add multiple tasks
        udin.getResponse("todo task 1");
        udin.getResponse("deadline task 2 /by 2024-12-25 1200");
        udin.getResponse("event task 3 /from 2024-12-25 1200 /to 2024-12-25 1400");
        udin.getResponse("mark 1");
        udin.getResponse("bye"); // Save all tasks
        
        // Create new Udin instance to test loading
        Udin newUdin = new Udin(testFilePath);
        String response = newUdin.getResponse("list");
        assertTrue(response.contains("task 1"));
        assertTrue(response.contains("task 2"));
        assertTrue(response.contains("task 3"));
        assertTrue(response.contains("[X]")); // Marked task should be done
    }

    @Test
    public void testHelpContent() {
        String help = Udin.HELP;
        assertTrue(help.contains("list"));
        assertTrue(help.contains("bye"));
        assertTrue(help.contains("mark"));
        assertTrue(help.contains("unmark"));
        assertTrue(help.contains("todo"));
        assertTrue(help.contains("deadline"));
        assertTrue(help.contains("event"));
        assertTrue(help.contains("delete"));
        assertTrue(help.contains("help"));
        assertTrue(help.contains("yyyy-mm-dd hhmm"));
    }
}
