public class Deadline extends Task{
    private String deadline;

    public Deadline(String title, String deadline) {
        super(title);
        this.deadline = deadline;
    }

    @Override
    public String display() {
        return "[D]" + super.display() + " (by: " + this.deadline + ")";
    }
}
