import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Manages the task operations for the task management application
 */
public class TaskManager {

  private ArrayList<Task> taskList; // Stores all the task objects
  private File file; // File object for performing file operations
  private String filePath; // Stores the absolute path of the text file
  private Stack<TaskSnapshot> history; // Stack to store the states of all the task objects in the arraylsit for undo functionality
  private Properties properties; // Properties object to manage the configuration properties

  /**
   * Constructs a TaskManager instance, initializes necessary components, and read
   * tasks from disk if the file exists
   */
  TaskManager() {
    taskList = new ArrayList<>();
    history = new Stack<>();
    filePath = getFilePathFromConfig();
    file = new File(filePath);
    if (!file.exists()) {
      try {
        if (file.createNewFile()) {
          System.out.println("file created");
        }
      } catch (IOException e) {
        System.err.println("Enter a valid file path " + e);
      }
    }
    readTasksFromDisk();
  }

  /**
   * Gets the file path from the configuration file if it exists, otherwise it
   * prompts the user to input the file path
   * 
   * @return the file path
   */
  private String getFilePathFromConfig() {
    File configFile = new File("config.properties");
    properties = new Properties();
    if (configFile.exists()) {
      try (InputStream input = new FileInputStream(configFile)) {
        properties.load(input);
      } catch (FileNotFoundException e) {
        System.err.println("Error finding the file");
      } catch (IOException e) {
        System.err.println("Error reading the input stream " + e);
      }
    }
    String path = properties.getProperty("filePath");
    if ((path != null) && (!path.isEmpty())) {
      return path;
    }
    return getUserInputFilePath();
  }

  /**
   * Prompts the user to input the file path for storing tasks
   * 
   * @return the user provided file path
   */
  private String getUserInputFilePath() {
    Scanner input = new Scanner(System.in);
    System.out.println("Welcome to my todo list application !!");
    System.out.println("Enter the file path(String format) in which you want to store your tasks");
    String path = input.nextLine();
    path = path + "\\MyTasks.txt";
    saveFilePathToConfig(path);
    return path;
  }

  /**
   * Saves the given file path to the configuration file
   * 
   * @param path the file path to save
   */
  private void saveFilePathToConfig(String filePath) {
    properties = new Properties();
    properties.setProperty("filePath", filePath);
    try (OutputStream output = new FileOutputStream("config.properties")) {
      properties.store(output, null);
    } catch (FileNotFoundException e) {
      System.err.println("Error finding the config file " + e);
    } catch (IOException e) {
      System.err.println("Error writing to the config file" + e);
    }
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
   * Deletes all tasksand takes a snapshot of the current state for undo
   * functionality
   */
  public void deleteAllTasks() {
    takeSnapShot();
    taskList.clear();
    System.out.println("All tasks deleted");
  }

  /**
   * Returns the total count of all available tasks
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
   * Writes all tasks to the disk
   */
  public void writeTasksToDisk() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
      for (Task task : taskList) {
        bw.write(task.getDescription() + "," + task.getStatus());
        bw.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error writing tasks to disk " + e);
    }
  }

  /**
   * Reads all tasks from the disk and stores them in the task list
   */
  public void readTasksFromDisk() {
    if (!file.exists()) {
      System.out.println("file doesnt exists");
    }
    String str;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      while ((str = br.readLine()) != null) {
        String[] parts = str.split(",");
        Task task;
        if (parts.length == 2) {
          task = new Task(parts[0]);
          if (("COMPLETED").equals(parts[1])) {
            task.markComplete();
          }
          taskList.add(task);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading tasks from disk " + e);
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

}
