package udin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {
    private Deadline deadline = new Deadline("test", "2000-12-12 1800");

    @Test
    public void displayTest() {
        assertEquals("[D][ ] test (by: Dec 12 2000, 6:00PM)", deadline.display());
        deadline.mark();
        assertEquals("[D][X] test (by: Dec 12 2000, 6:00PM)", deadline.display());
        deadline.unmark();
    }

    @Test
    public void toSaveFormatTest() {
        assertEquals("D,0,test,2000-12-12 1800", deadline.toSaveFormat());
        deadline.mark();
        assertEquals("D,1,test,2000-12-12 1800", deadline.toSaveFormat());
        deadline.unmark();
    }
}
