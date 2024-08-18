import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;

/**
 * Manages the task operations for the task management application
 */
public class TaskManager {

  private ArrayList<Task> taskList; // Stores all the task objects
  private Stack<TaskSnapshot> history; // Stack to store the states of all the task objects in the arraylsit for undo
                                       // functionality
  private FileManager fileManager;
  UserInput userInput;
  Scanner scanner;

  /**
   * Constructs the Taskmanager and FileManager instance, and reads all tasks from
   * the disk when the system starts.
   */
  TaskManager() {
    this.history = new Stack<>();
    this.taskList = new ArrayList<>();
    userInput = new UserInput();
    fileManager = new FileManager();
    scanner = new Scanner(System.in);
    fileManager.setResources(taskList);
    userInput.setResources(this, fileManager);
    fileManager.readTasksFromDisk();
    userInput.start();
  }

  private Task findTask(String description) {
    for (Task task : taskList) {
      if (task.getDescription().equals(description)) {
        return task;
      }
    }
    return null;
  }

  /**
   * Adds the given task to the tasklist and takes a snapshot of the current state
   * for undo functionality
   * 
   * @param description the task description
   */
  public void addTask(String description) {
    takeSnapShot();
    Task task1 = new Task(description);
    taskList.add(task1);
    System.out.println("task added ");
  }

  /**
   * Marks the given task as completed and takes a snapshot of the current state
   * for undo functionality
   * 
   * @param description the task description
   */
  public void completeTask(String description) {
    takeSnapShot();
    Task task = findTask(description);
    if (task != null) {
      task.markComplete();
      System.out.println("Task completed");
    } else {
      System.out.println("Task not found");
    }
  }

  /**
   * Deletes the given task and takes a snapshot of the current state for undo
   * functionality
   * 
   * @param description the task description
   */
  public void deleteTask(String description) {
    takeSnapShot();
    Task task = findTask(description);
    if (task != null) {
      taskList.remove(task);
      System.out.println("Task removed");
    } else {
      System.out.println("Task not found");
    }
  }

  /**
   * Deletes all tasks and takes a snapshot of the current state for undo
   * functionality
   */
  public void deleteAllTasks() {
    if(!taskList.isEmpty()){
      takeSnapShot();
      taskList.clear();
      System.out.println("All tasks deleted");
    }else{
      System.out.println("No tasks available");
    }
  }

  /**
   * Returns the total count of all tasks
   * 
   * @return the number of tasks
   */
  public int getNoOfTasks() {
    return taskList.size();
  }

  /**
   * Displays all the available tasks and prints a message if its empty
   */
  public void viewAllTasks() {
    if (taskList.isEmpty()) {
      System.out.println("Task list is empty");
      return;
    }
    taskList.forEach(task -> System.out.println(task.toString()));
  }

  /**
   * Searches for a particular task and displays its details if found
   * 
   * @param description the task description
   */
  public void searchTask(String description) {
    Task task = findTask(description);
    if (task != null) {
      System.out.println(task.toString());
    } else {
      System.out.println("Task not found");
    }
  }

  /**
   * Displays all the completed tasks
   */
  public void viewCompletedTasks() {
    int count = 0;
    for (Task task : taskList) {
      if (task.getStatus().equals("COMPLETED")) {
        count++;
        System.out.println(task.toString());
      }
    }
    if (count == 0) {
      System.out.println("\nThere are no completed tasks.");
    }
  }

  /**
   * Displays all the incompleted tasks
   */
  public void viewIncompletedTasks() {
    int count = 0;
    for (Task task : taskList) {
      if (!task.getStatus().equals("COMPLETED")) {
        count++;
        System.out.println(task.toString());
      }
    }
    if (count == 0) {
      System.out.println("\nThere are no incomplete tasks");
    }
  }

  /**
   * Takes a snapshot of the current state for undo functionality
   */
  public void takeSnapShot() {
    history.push(new TaskSnapshot(taskList));
  }

  /**
   * Undo the last operation by restoring the previous state of the task list
   */
  public void undo() {
    if (!history.empty()) {
      TaskSnapshot previousState = history.pop();
      taskList = previousState.getState();
      System.out.println("undo successful");
    } else {
      System.out.println("no operations to undo");
    }
  }
  public static void main(String[] args) {
    TaskManager taskManager = new TaskManager();
    
  }
}
