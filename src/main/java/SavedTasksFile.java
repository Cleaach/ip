import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SavedTasksFile extends File {
    public SavedTasksFile(String pathname) {
        super(pathname);
    }

    public boolean isCorrectlyFormatted() {
        try (Scanner sc = new Scanner(this)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                String type = parts[0];

                switch (type) {
                    case "T": // T,done,desc
                        if (parts.length < 3) return false;
                        if (!isValidDone(parts[1])) return false;
                        break;
                    case "D": // D,done,desc,by
                        if (parts.length < 4) return false;
                        if (!isValidDone(parts[1])) return false;
                        break;
                    case "E": // E,done,desc,from,to
                        if (parts.length < 5) return false;
                        if (!isValidDone(parts[1])) return false;
                        break;
                    default:
                        return false;
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    private boolean isValidDone(String s) {
        return s.equals("0") || s.equals("1");
    }
}
