package udin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TaskListTest {
    private TaskList taskList;
    private ToDo todo1;
    private ToDo todo2;
    private Deadline deadline;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo1 = new ToDo("Test task 1");
        todo2 = new ToDo("Test task 2");
        deadline = new Deadline("Test deadline", "2024-12-25 1200");
    }

    @Test
    public void testEmptyTaskList() {
        assertEquals(0, taskList.size());
        assertTrue(taskList.getAll().isEmpty());
    }

    @Test
    public void testAddTask() {
        taskList.add(todo1);
        assertEquals(1, taskList.size());
        assertEquals(todo1, taskList.get(0));
    }

    @Test
    public void testAddMultipleTasks() {
        taskList.add(todo1);
        taskList.add(todo2);
        taskList.add(deadline);
        assertEquals(3, taskList.size());
    }

    @Test
    public void testRemoveTask() {
        taskList.add(todo1);
        taskList.add(todo2);
        
        Task removed = taskList.remove(0);
        assertEquals(todo1, removed);
        assertEquals(1, taskList.size());
        assertEquals(todo2, taskList.get(0));
    }

    @Test
    public void testRemoveTaskInvalidIndex() {
        taskList.add(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(1));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.remove(-1));
    }

    @Test
    public void testMarkTask() {
        taskList.add(todo1);
        assertFalse(todo1.isDone);
        
        taskList.mark(0);
        assertTrue(todo1.isDone);
    }

    @Test
    public void testUnmarkTask() {
        taskList.add(todo1);
        todo1.mark();
        assertTrue(todo1.isDone);
        
        taskList.unmark(0);
        assertFalse(todo1.isDone);
    }

    @Test
    public void testMarkUnmarkInvalidIndex() {
        taskList.add(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.mark(1));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.unmark(-1));
    }

    @Test
    public void testGetTask() {
        taskList.add(todo1);
        taskList.add(todo2);
        
        assertEquals(todo1, taskList.get(0));
        assertEquals(todo2, taskList.get(1));
    }

    @Test
    public void testGetTaskInvalidIndex() {
        taskList.add(todo1);
        
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.get(-1));
    }

    @Test
    public void testFindTasksByKeyword() {
        taskList.add(new ToDo("Buy groceries"));
        taskList.add(new ToDo("Buy milk"));
        taskList.add(new ToDo("Study for exam"));
        taskList.add(new Deadline("Buy birthday gift", "2024-12-25 1200"));
        
        List<Task> buyTasks = taskList.findTasksByKeyword("buy");
        assertEquals(3, buyTasks.size());
        
        List<Task> studyTasks = taskList.findTasksByKeyword("study");
        assertEquals(1, studyTasks.size());
        assertEquals("Study for exam", studyTasks.get(0).getTitle());
        
        List<Task> emptyTasks = taskList.findTasksByKeyword("nonexistent");
        assertTrue(emptyTasks.isEmpty());
    }

    @Test
    public void testFindTasksCaseInsensitive() {
        taskList.add(new ToDo("Buy groceries"));
        taskList.add(new ToDo("BUY MILK"));
        taskList.add(new ToDo("buy bread"));
        
        List<Task> buyTasks = taskList.findTasksByKeyword("BUY");
        assertEquals(3, buyTasks.size());
        
        List<Task> milkTasks = taskList.findTasksByKeyword("milk");
        assertEquals(1, milkTasks.size());
    }

    @Test
    public void testShowEmptyList() {
        String result = taskList.show();
        assertTrue(result.contains("Your tasks:"));
        assertTrue(result.contains("0.") == false); // No tasks should be shown
    }

    @Test
    public void testShowWithTasks() {
        taskList.add(todo1);
        taskList.add(deadline);
        
        String result = taskList.show();
        assertTrue(result.contains("Your tasks:"));
        assertTrue(result.contains("1."));
        assertTrue(result.contains("2."));
        assertTrue(result.contains(todo1.display()));
        assertTrue(result.contains(deadline.display()));
    }

    @Test
    public void testConstructorWithInitialTasks() {
        List<Task> initialTasks = new ArrayList<>();
        initialTasks.add(todo1);
        initialTasks.add(todo2);
        
        TaskList newTaskList = new TaskList(initialTasks);
        assertEquals(2, newTaskList.size());
        assertEquals(todo1, newTaskList.get(0));
        assertEquals(todo2, newTaskList.get(1));
    }

    @Test
    public void testGetAllReturnsDirectReference() {
        taskList.add(todo1);
        List<Task> allTasks = taskList.getAll();
        
        // Modifying the returned list should affect the TaskList
        allTasks.add(todo2);
        assertEquals(2, taskList.size());
        assertEquals(todo2, taskList.get(1));
    }
}

