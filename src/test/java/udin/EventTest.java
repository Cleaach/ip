package udin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {
    private Event event = new Event("test", "2000-12-12 1800", "2001-11-11 0000");

    @Test
    public void displayTest() {
        assertEquals("[E][ ] test (from: Dec 12 2000, 6:00PM to: Nov 11 2001, 12:00AM)", event.display());
        event.mark();
        assertEquals("[E][X] test (from: Dec 12 2000, 6:00PM to: Nov 11 2001, 12:00AM)", event.display());
        event.unmark();
    }

    @Test
    public void toSaveFormatTest() {
        assertEquals("E,0,test,2000-12-12T18:00,2001-11-11T00:00", event.toSaveFormat());
        event.mark();
        assertEquals("E,1,test,2000-12-12T18:00,2001-11-11T00:00", event.toSaveFormat());
        event.unmark();
    }
}
