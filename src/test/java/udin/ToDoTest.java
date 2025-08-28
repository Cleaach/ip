package udin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    private ToDo todo = new ToDo("test");

    @Test
    public void displayTest() {
        assertEquals("[T][ ] test", todo.display());
        todo.mark();
        assertEquals("[T][X] test", todo.display());
        todo.unmark();
    }

    @Test
    public void toSaveFormatTest() {
        assertEquals("T,0,test", todo.toSaveFormat());
        todo.mark();
        assertEquals("T,1,test", todo.toSaveFormat());
        todo.unmark();
    }
}
