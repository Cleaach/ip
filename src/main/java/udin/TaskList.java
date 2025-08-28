package udin;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public void add(Task t) { tasks.add(t); }

    public Task remove(int index) { return tasks.remove(index); }

    public void mark(int index) { tasks.get(index).mark(); }

    public void unmark(int index) { tasks.get(index).unmark(); }

    public Task get(int index) { return tasks.get(index); }

    public List<Task> getAll() { return tasks; }

    public int size() { return tasks.size(); }

    /**
     * Returns a list of tasks whose title contains the specified keyword (case-insensitive).
     *
     * @param keyword the keyword to search for in task titles
     * @return a list of tasks matching the keyword
     */
    public List<Task> findTasksByKeyword(String keyword) {
        List<Task> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getTitle().toLowerCase().contains(lowerKeyword)) {
                results.add(task);
            }
        }
        return results;
    }

}
