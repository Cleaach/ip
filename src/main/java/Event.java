public class Event extends Task {
    private String start;
    private String end;

    public Event(String title, String start, String end) {
        super(title);
        this.start = start;
        this.end = end;
    }

    @Override
    public String display() {
        return "[E]" + super.display() + " (from: " + this.start + " to: " + this.end + ")";
    }
}
