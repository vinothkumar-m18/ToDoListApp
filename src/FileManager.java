import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

/**
 * Represents a file manager class which handles reading and writing of tasklist to disk
 */
public class FileManager {
  
  private File file; // Stores the file object 
  private ArrayList<Task> taskList; // Stores the list of tasks

  public void setResources(ArrayList<Task> taskList){
    this.taskList = taskList;
    this.file = new File("src/MyTasks.txt");
  }

  /**
   * Reads all tasks from the disk and stores them in the task list
   */
  public void readTasksFromDisk() {
    if (!file.exists()) {
      System.out.println("file doesnt exists");
      System.out.println("Please add a text file named \"MyTasks.txt\" inside the src folder");
      System.out.println("Exited the application");
      System.exit(1);
    }
    String str;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      while ((str = br.readLine()) != null) {
        String[] parts = str.split("\\|");
        Task task;
        if (parts.length == 2) {
          task = new Task(parts[0]);
          if (parts[1].equals("COMPLETED")) {
            task.markComplete();
          }
          taskList.add(task);
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading tasks from disk " + e);
      System.out.println("Exited the application");
      System.exit(1);
    }
  }

  /**
   * Writes all the tasks in the tasklist to the disk
   */
  public void writeTasksToDisk() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
      for (Task task : taskList) {
        bw.write(task.getDescription() + "|" + task.getStatus());
        bw.newLine();
      }
    } catch (IOException e) {
      System.err.println("Error writing tasks to disk " + e);
      System.out.println("Exited the application");
      System.exit(0);
    }
  }

}
