package udin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StorageTest {
    private Storage storage;
    private String testFilePath = "test_storage.txt";
    private File testFile;

    @BeforeEach
    public void setUp() {
        storage = new Storage(testFilePath);
        testFile = new File(testFilePath);
    }

    @AfterEach
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFileNotFound() {
        assertThrows(Exception.class, () -> storage.load());
    }

    @Test
    public void testLoadEmptyFile() throws Exception {
        testFile.createNewFile();
        List<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testLoadWithToDoTasks() throws Exception {
        createTestFile("T,0,test todo 1\nT,1,test todo 2\n");
        List<Task> tasks = storage.load();
        
        assertEquals(2, tasks.size());
        assertEquals("test todo 1", tasks.get(0).getTitle());
        assertFalse(tasks.get(0).isDone);
        assertEquals("test todo 2", tasks.get(1).getTitle());
        assertTrue(tasks.get(1).isDone);
    }

    @Test
    public void testLoadWithDeadlineTasks() throws Exception {
        createTestFile("D,0,test deadline,2024-12-25 1200\nD,1,another deadline,2024-12-26 1400\n");
        List<Task> tasks = storage.load();
        
        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
        assertEquals("test deadline", tasks.get(0).getTitle());
        assertFalse(tasks.get(0).isDone);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertEquals("another deadline", tasks.get(1).getTitle());
        assertTrue(tasks.get(1).isDone);
    }

    @Test
    public void testLoadWithEventTasks() throws Exception {
        createTestFile("E,0,test event,2024-12-25 1200,2024-12-25 1400\nE,1,another event,2024-12-26 1000,2024-12-26 1200\n");
        List<Task> tasks = storage.load();
        
        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof Event);
        assertEquals("test event", tasks.get(0).getTitle());
        assertFalse(tasks.get(0).isDone);
        assertTrue(tasks.get(1) instanceof Event);
        assertEquals("another event", tasks.get(1).getTitle());
        assertTrue(tasks.get(1).isDone);
    }

    @Test
    public void testLoadWithMixedTasks() throws Exception {
        createTestFile("T,0,todo task\nD,1,deadline task,2024-12-25 1200\nE,0,event task,2024-12-25 1200,2024-12-25 1400\n");
        List<Task> tasks = storage.load();
        
        assertEquals(3, tasks.size());
        assertTrue(tasks.get(0) instanceof ToDo);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertTrue(tasks.get(2) instanceof Event);
    }

    @Test
    public void testLoadWithEmptyLines() throws Exception {
        createTestFile("T,0,task 1\n\nT,1,task 2\n   \nT,0,task 3\n");
        List<Task> tasks = storage.load();
        
        assertEquals(3, tasks.size());
        assertEquals("task 1", tasks.get(0).getTitle());
        assertEquals("task 2", tasks.get(1).getTitle());
        assertEquals("task 3", tasks.get(2).getTitle());
    }

    @Test
    public void testLoadWithInvalidTaskType() throws Exception {
        createTestFile("T,0,valid task\nX,1,invalid task\nT,0,another valid task\n");
        assertThrows(Exception.class, () -> storage.load());
    }

    @Test
    public void testLoadWithInvalidDateFormat() throws Exception {
        createTestFile("D,0,test,invalid-date\n");
        assertThrows(Exception.class, () -> storage.load());
    }

    @Test
    public void testSaveEmptyTaskList() throws IOException {
        List<Task> tasks = List.of();
        storage.save(tasks);
        
        assertTrue(testFile.exists());
        assertEquals(0, testFile.length());
    }

    @Test
    public void testSaveToDoTasks() throws IOException {
        List<Task> tasks = List.of(
            new ToDo("test todo 1"),
            new ToDo("test todo 2")
        );
        tasks.get(1).mark();
        
        storage.save(tasks);
        
        assertTrue(testFile.exists());
        String content = readTestFile();
        assertTrue(content.contains("T,0,test todo 1"));
        assertTrue(content.contains("T,1,test todo 2"));
    }

    @Test
    public void testSaveDeadlineTasks() throws IOException {
        List<Task> tasks = List.of(
            new Deadline("test deadline", "2024-12-25 1200"),
            new Deadline("another deadline", "2024-12-26 1400")
        );
        tasks.get(1).mark();
        
        storage.save(tasks);
        
        assertTrue(testFile.exists());
        String content = readTestFile();
        assertTrue(content.contains("D,0,test deadline,2024-12-25 1200"));
        assertTrue(content.contains("D,1,another deadline,2024-12-26 1400"));
    }

    @Test
    public void testSaveEventTasks() throws IOException {
        List<Task> tasks = List.of(
            new Event("test event", "2024-12-25 1200", "2024-12-25 1400"),
            new Event("another event", "2024-12-26 1000", "2024-12-26 1200")
        );
        tasks.get(1).mark();
        
        storage.save(tasks);
        
        assertTrue(testFile.exists());
        String content = readTestFile();
        assertTrue(content.contains("E,0,test event,2024-12-25 1200,2024-12-25 1400"));
        assertTrue(content.contains("E,1,another event,2024-12-26 1000,2024-12-26 1200"));
    }

    @Test
    public void testSaveMixedTasks() throws IOException {
        List<Task> tasks = List.of(
            new ToDo("todo task"),
            new Deadline("deadline task", "2024-12-25 1200"),
            new Event("event task", "2024-12-25 1200", "2024-12-25 1400")
        );
        
        storage.save(tasks);
        
        assertTrue(testFile.exists());
        String content = readTestFile();
        assertTrue(content.contains("T,0,todo task"));
        assertTrue(content.contains("D,0,deadline task,2024-12-25 1200"));
        assertTrue(content.contains("E,0,event task,2024-12-25 1200,2024-12-25 1400"));
    }

    @Test
    public void testSaveCreatesDirectory() throws IOException {
        Storage storageWithDir = new Storage("test_dir/test_storage.txt");
        List<Task> tasks = List.of(new ToDo("test"));
        
        storageWithDir.save(tasks);
        
        File testDirFile = new File("test_dir/test_storage.txt");
        assertTrue(testDirFile.exists());
        
        // Clean up
        testDirFile.delete();
        new File("test_dir").delete();
    }

    @Test
    public void testRoundTripSaveAndLoad() throws Exception {
        List<Task> originalTasks = List.of(
            new ToDo("todo task"),
            new Deadline("deadline task", "2024-12-25 1200"),
            new Event("event task", "2024-12-25 1200", "2024-12-25 1400")
        );
        originalTasks.get(1).mark();
        
        // Save tasks
        storage.save(originalTasks);
        
        // Load tasks
        List<Task> loadedTasks = storage.load();
        
        assertEquals(originalTasks.size(), loadedTasks.size());
        for (int i = 0; i < originalTasks.size(); i++) {
            assertEquals(originalTasks.get(i).getClass(), loadedTasks.get(i).getClass());
            assertEquals(originalTasks.get(i).getTitle(), loadedTasks.get(i).getTitle());
            assertEquals(originalTasks.get(i).isDone, loadedTasks.get(i).isDone);
        }
    }

    private void createTestFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(content);
        }
    }

    private String readTestFile() throws IOException {
        return new String(java.nio.file.Files.readAllBytes(testFile.toPath()));
    }
}

