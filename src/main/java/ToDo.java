public class ToDo extends Task {

    public ToDo(String title) {
        super(title);
    }

    @Override
    public String display() {
        return "[T]" + super.display();
    }
}