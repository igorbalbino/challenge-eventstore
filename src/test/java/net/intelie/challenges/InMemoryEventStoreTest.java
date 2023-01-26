import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InMemoryEventStoreTest {
    private InMemoryEventStore store;

    @Before
    public void setUp() {
        store = new InMemoryEventStore();
    }

    @Test
    public void testInsert() {
        Event event = new Event("type1", 1);
        store.insert(event);
        List<Event> events = store.query("type1", 0, Long.MAX_VALUE);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));
    }

    @Test
    public void testRemoveAll() {
        Event event1 = new Event("type1", 1);
        Event event2 = new Event("type2", 2);
        Event event3 = new Event("type1", 3);
        store.insert(event1);
        store.insert(event2);
        store.insert(event3);

        store.removeAll("type1");
        List<Event> events = store.query("type1", 0, Long.MAX_VALUE);
        assertEquals(0, events.size());

        events = store.query("type2", 0, Long.MAX_VALUE);
        assertEquals(1, events.size());
        assertEquals(event2, events.get(0));
    }
}