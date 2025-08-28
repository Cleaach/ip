package udin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // loads tasks from the file; throws IOException if file missing or IO error
    public List<Task> load() throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new FileNotFoundException("udin.Storage file not found: " + filePath);
        }
        List<Task> tasks = new ArrayList<>();
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                String type = parts[0];
                boolean done = parts[1].equals("1");
                Task t = null;
                switch (type) {
                    case "T":
                        t = new ToDo(parts[2]);
                        break;
                    case "D":
                        t = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        t = new Event(parts[2], parts[3], parts[4]);
                        break;
                    default:
                        // ignore unknown lines (or throw)
                }
                if (t != null) {
                    if (done) t.mark();
                    tasks.add(t);
                }
            }
        }
        return tasks;
    }

    // saves the given list (overwrites)
    public void save(List<Task> tasks) throws IOException {
        File parent = new File(filePath).getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (FileWriter fw = new FileWriter(filePath)) {
            for (Task t : tasks) {
                fw.write(t.toSaveFormat());
                fw.write(System.lineSeparator());
            }
        }
    }
}
