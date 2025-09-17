package udin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    private TaskList taskList;
    private Storage storage;
    private String testFilePath = "test_tasks.txt";

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        storage = new Storage(testFilePath);
    }

    // Test command detection methods
    @Test
    public void testCommandDetection() {
        assertTrue(Parser.isList("list"));
        assertTrue(Parser.isBye("bye"));
        assertTrue(Parser.isMark("mark 1"));
        assertTrue(Parser.isUnmark("unmark 1"));
        assertTrue(Parser.isTodo("todo test"));
        assertTrue(Parser.isDeadline("deadline test /by 2024-12-25 1200"));
        assertTrue(Parser.isEvent("event test /from 2024-12-25 1200 /to 2024-12-25 1400"));
        assertTrue(Parser.isDelete("delete 1"));
        assertTrue(Parser.isHelp("help"));
        
        assertFalse(Parser.isList("notlist"));
        assertFalse(Parser.isBye("notbye"));
        assertFalse(Parser.isMark("notmark"));
        assertFalse(Parser.isTodo("notodo"));
    }

    @Test
    public void testParseIndexValidInput() {
        assertEquals(0, Parser.parseIndex("mark 1"));
        assertEquals(1, Parser.parseIndex("delete 2"));
        assertEquals(4, Parser.parseIndex("unmark 5"));
    }

    @Test
    public void testParseIndexInvalidInput() {
        // Test insufficient parts
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex("mark"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex(""));
        
        // Test non-integer input
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex("mark abc"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex("delete xyz"));
        
        // Test negative numbers
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex("mark 0"));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseIndex("delete -1"));
    }

    @Test
    public void testParseDeadlineParts() {
        String[] parts = Parser.parseDeadlineParts("deadline test task /by 2024-12-25 1200");
        assertEquals(2, parts.length);
        assertEquals("test task", parts[0]);
        assertEquals("2024-12-25 1200", parts[1]);
    }

    @Test
    public void testParseDeadlinePartsMissingBy() {
        String[] parts = Parser.parseDeadlineParts("deadline test task");
        assertEquals(2, parts.length);
        assertEquals("test task", parts[0]);
        assertEquals("", parts[1]);
    }

    @Test
    public void testParseEventParts() {
        String[] parts = Parser.parseEventParts("event test /from 2024-12-25 1200 /to 2024-12-25 1400");
        assertEquals(3, parts.length);
        assertEquals("test", parts[0]);
        assertEquals("2024-12-25 1200", parts[1]);
        assertEquals("2024-12-25 1400", parts[2]);
    }

    @Test
    public void testExecuteCommandList() {
        taskList.add(new ToDo("test task"));
        String result = Parser.executeCommand("list", taskList, storage);
        assertTrue(result.contains("Your tasks:"));
        assertTrue(result.contains("test task"));
    }

    @Test
    public void testExecuteCommandHelp() {
        String result = Parser.executeCommand("help", taskList, storage);
        assertEquals(Udin.HELP, result);
    }

    @Test
    public void testExecuteCommandTodo() {
        String result = Parser.executeCommand("todo test task", taskList, storage);
        assertTrue(result.contains("Got it. I've added this task:"));
        assertTrue(result.contains("test task"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void testExecuteCommandTodoEmpty() {
        String result = Parser.executeCommand("todo", taskList, storage);
        assertEquals("The description of a todo cannot be empty.", result);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandTodoBlank() {
        String result = Parser.executeCommand("todo   ", taskList, storage);
        assertEquals("The description of a todo cannot be empty.", result);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandDeadline() {
        String result = Parser.executeCommand("deadline test /by 2024-12-25 1200", taskList, storage);
        assertTrue(result.contains("Got it. I've added this task:"));
        assertTrue(result.contains("test"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void testExecuteCommandDeadlineInvalidDate() {
        String result = Parser.executeCommand("deadline test /by invalid-date", taskList, storage);
        assertEquals("Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).", result);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandDeadlineEmptyDescription() {
        String result = Parser.executeCommand("deadline /by 2024-12-25 1200", taskList, storage);
        assertEquals("The description or /by date of a deadline cannot be empty.", result);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandEvent() {
        String result = Parser.executeCommand("event test /from 2024-12-25 1200 /to 2024-12-25 1400", taskList, storage);
        assertTrue(result.contains("Got it. I've added this task:"));
        assertTrue(result.contains("test"));
        assertEquals(1, taskList.size());
    }

    @Test
    public void testExecuteCommandEventInvalidDate() {
        String result = Parser.executeCommand("event test /from 2024-13-45 2500 /to 2024-12-25 1400", taskList, storage);
        assertEquals("Please enter dates as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).", result);
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandMark() {
        taskList.add(new ToDo("test task"));
        String result = Parser.executeCommand("mark 1", taskList, storage);
        assertTrue(result.contains("Good boy! This task is all done:"));
        assertTrue(taskList.get(0).isDone);
    }

    @Test
    public void testExecuteCommandMarkInvalidIndex() {
        String result = Parser.executeCommand("mark 1", taskList, storage);
        assertEquals("Invalid task number.", result);
    }

    @Test
    public void testExecuteCommandUnmark() {
        ToDo todo = new ToDo("test task");
        todo.mark();
        taskList.add(todo);
        
        String result = Parser.executeCommand("unmark 1", taskList, storage);
        assertTrue(result.contains("This task was unmarked:"));
        assertFalse(taskList.get(0).isDone);
    }

    @Test
    public void testExecuteCommandDelete() {
        taskList.add(new ToDo("test task"));
        String result = Parser.executeCommand("delete 1", taskList, storage);
        assertTrue(result.contains("Noted. I've removed this task:"));
        assertEquals(0, taskList.size());
    }

    @Test
    public void testExecuteCommandFind() {
        taskList.add(new ToDo("buy groceries"));
        taskList.add(new ToDo("study for exam"));
        taskList.add(new ToDo("buy milk"));
        
        String result = Parser.executeCommand("find buy", taskList, storage);
        assertTrue(result.contains("Here are the matching tasks in your list:"));
        assertTrue(result.contains("buy groceries"));
        assertTrue(result.contains("buy milk"));
        assertFalse(result.contains("study for exam"));
    }

    @Test
    public void testExecuteCommandFindEmpty() {
        String result = Parser.executeCommand("find", taskList, storage);
        assertEquals("Please provide a keyword to find.", result);
    }

    @Test
    public void testExecuteCommandFindBlank() {
        String result = Parser.executeCommand("find   ", taskList, storage);
        assertEquals("Please provide a keyword to find.", result);
    }

    @Test
    public void testExecuteCommandFindNoResults() {
        taskList.add(new ToDo("study for exam"));
        String result = Parser.executeCommand("find buy", taskList, storage);
        assertEquals("No tasks found matching your search.", result);
    }

    @Test
    public void testExecuteCommandUnrecognized() {
        String result = Parser.executeCommand("unknown command", taskList, storage);
        assertEquals("Unrecognized command: unknown command. Type 'help' to see the list of commands.", result);
    }

    @Test
    public void testExecuteCommandBye() {
        taskList.add(new ToDo("test task"));
        String result = Parser.executeCommand("bye", taskList, storage);
        assertEquals("Bye. Hope to see you again soon!", result);
    }

    @Test
    public void testFormatFoundTasks() {
        taskList.add(new ToDo("task 1"));
        taskList.add(new ToDo("task 2"));
        
        String result = Parser.executeCommand("find task", taskList, storage);
        // This will call formatFoundTasks internally
        assertTrue(result.contains("Here are the matching tasks in your list:"));
    }

    @Test
    public void testFormatFoundTasksEmpty() {
        String result = Parser.executeCommand("find nonexistent", taskList, storage);
        assertEquals("No tasks found matching your search.", result);
    }
}
