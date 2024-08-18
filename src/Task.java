/**
 * Represents a task which has a description and status.
 */
public class Task {
    private final String taskDescription; // To store the task description
    private boolean isCompleted; // To store the task status

    /**
     * Initializes the task with the given description
     * 
     * @param task the description of the task
     */
    public Task(String task) {
        this.taskDescription = task;
        isCompleted = false; // Initializing every task with incomplete status as default
    }

    /**
     * Returns the task description
     * 
     * @return the description of the task
     */
    public String getDescription() {
        return this.taskDescription;
    }

    /**
     * Returns the current status of the given task
     * 
     * @return "COMPLETED" when the task is completed, otherwise "INCOMPLETE"
     */
    public String getStatus() {
        return (isCompleted) ? "COMPLETED" : "INCOMPLETE";
    }

    /**
     * Marks a given task as completed
     */
    public void markComplete() {
        this.isCompleted = true;
    }

    /**
     * Returns the task description and status in a readable format
     */
    public String toString(){
        return "\n{description = '" + taskDescription + "', Status = '" + getStatus() + "'}";
    }
}
