package udin;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of {@link Task} objects.
 * <p>
 * Provides operations to add, remove, retrieve, and update
 * the completion status of tasks. Internally uses
 * an {@link ArrayList} to store the tasks.
 */
public class TaskList {
    /**
     * The internal list holding the tasks.
     */
    private final List<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list initialized with a given collection of tasks.
     *
     * @param initial the tasks to initialize this list with
     */
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Adds a task to the list.
     *
     * @param t the task to be added
     */
    public void add(Task t) { tasks.add(t); }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the zero-based index of the task to remove
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task remove(int index) { return tasks.remove(index); }

    /**
     * Marks the task at the specified index as completed.
     *
     * @param index the zero-based index of the task to mark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void mark(int index) { tasks.get(index).mark(); }

    /**
     * Marks the task at the specified index as not completed.
     *
     * @param index the zero-based index of the task to unmark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void unmark(int index) { tasks.get(index).unmark(); }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the zero-based index of the task to retrieve
     * @return the task at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int index) { return tasks.get(index); }

    /**
     * Returns the entire list of tasks.
     * <p>
     * Note: Since the internal list is directly returned, external
     * modifications will affect this TaskList.
     *
     * @return the list of all tasks
     */
    public List<Task> getAll() { return tasks; }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() { return tasks.size(); }

    public String show() {
        String res = "";
        res += "\n Your tasks:";
        for (int i = 0; i < tasks.size(); i++) {
            res += (" " + (i + 1) + "." + tasks.get(i).display() + "\n");
        }
        return res;
    }

}
