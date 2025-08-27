import java.io.FileWriter;

public class ToDo extends Task {

    public ToDo(String title) {
        super(title);
    }

    @Override
    public String display() {
        return "[T]" + super.display();
    }

    @Override
    public String toSaveFormat() {
        return "T," + (this.isDone ? "1" : "0") + "," + this.title;
    }
}