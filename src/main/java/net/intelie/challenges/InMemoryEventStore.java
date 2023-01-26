import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryEventStore implements EventStore {
    private List<Event> events;

    public InMemoryEventStore() {
        events = new ArrayList<>();
    }

    @Override
    public void insert(Event event) {
        events.add(event);
    }

    @Override
    public void removeAll(String type) {
        events.removeIf(event -> event.getType().equals(type));
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        return new EventIteratorImpl(events.iterator(), type, startTime, endTime);
    }

    private class EventIteratorImpl implements EventIterator {
        private Iterator<Event> iterator;
        private Event current;
        private String type;
        private long startTime;
        private long endTime;

        public EventIteratorImpl(Iterator<Event> iterator, String type, long startTime, long endTime) {
            this.iterator = iterator;
            this.type = type;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public boolean moveNext() {
            while (iterator.hasNext()) {
                current = iterator.next();
                if (current.getType().equals(type) && current.getTimestamp() >= startTime && current.getTimestamp() < endTime) {
                    return true;
                }
            }
            current = null;
            return false;
        }

        @Override
        public Event current() {
            if (current == null) {
                throw new IllegalStateException();
            }
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            iterator.remove();
        }

        @Override
        public void close() {
            // do nothing
        }
    }
}