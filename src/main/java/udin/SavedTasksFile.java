package udin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents the file used to persist saved tasks locally.
 * Provides validation to check whether the file is correctly formatted.
 *
 * @author Clement Chendra
 * @version 0.1
 * @since 0.1
 */
public class SavedTasksFile extends File {
    /**
     * Constructs a SavedTasksFile at the given path.
     *
     * @param pathname the path to the saved tasks file
     */
    public SavedTasksFile(String pathname) {
        super(pathname);
    }

    /**
     * Checks whether the file contents are valid according to the supported task formats.
     *
     * @return true if the file is correctly formatted, false otherwise
     */
    public boolean isCorrectlyFormatted() {
        try (Scanner sc = new Scanner(this)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", -1);
                String type = parts[0];

                switch (type) {
                    case "T":
                        if (parts.length < 3) return false;
                        if (!isValidDone(parts[1])) return false;
                        break;
                    case "D":
                        if (parts.length < 4) return false;
                        if (!isValidDone(parts[1])) return false;
                        break;
                    case "E":
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

    /**
     * Validates the done flag (should be "0" or "1").
     *
     * @param s the string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidDone(String s) {
        return s.equals("0") || s.equals("1");
    }
}
