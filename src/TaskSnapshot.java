import java.util.ArrayList;

/**
 * Represents a snapshot of the state of the current task list for undo
 * functionality
 */
public class TaskSnapshot {
  private ArrayList<Task> state; // Stores the state of the task list

  /**
   * Constructs a task snapshot for the given task list
   * 
   * @param state the state of the current task list
   */
  public TaskSnapshot(ArrayList<Task> state) {
    this.state = new ArrayList<>(state); // Creates a copy of the given state
  }

  /**
   * Returns the state of the task list stored in the snapshot
   * 
   * @return the state of the task list
   */
  public ArrayList<Task> getState() {
    return state;
  }

}
