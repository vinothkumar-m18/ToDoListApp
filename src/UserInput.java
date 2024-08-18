/**
 * Handles user interactions for the task management application
 */
public class UserInput {
  private String emptyTaskListMsg = "There are no tasks available. "; // Message for empty task list.
  private TaskManager taskManager;
  private FileManager fileManager;

  // Using constants for better readability
  private static final int MENU_ADD_TASK = 1;
  private static final int MENU_COMPLETE_TASK = 2;
  private static final int MENU_DELETE_TASK = 3;
  private static final int MENU_VIEW_TASKS = 4;
  private static final int MENU_SEARCH_TASK = 5;
  private static final int MENU_UNDO = 6;
  private static final int MENU_EXIT = 0;
  private static final String SUBMENU_EXIT = "3.0";
  private static final String SUBMENU_DELETE_TASK = "3.1";
  private static final String SUBMENU_DELETE_ALL_TASKS = "3.2";
  private static final String SUBMENU_VIEW_ALL_TASKS = "4.1";
  private static final String SUBMENU_VIEW_COMPLETED_TASKS = "4.2";
  private static final String SUBMENU_VIEW_INCOMPLETED_TASKS = "4.3";

  
  public void setResources(TaskManager taskManager, FileManager fileManager) {
    this.taskManager = taskManager;
    this.fileManager = fileManager;
  }

  /**
   * Checks whether the given task description is empty and prints a message if
   * its empty; based on the operation
   * 
   * @param taskInput the description of the task
   * @param operation the operation(add, delete, complete)
   * @return true if the task description is empty, false otherwise
   */
  public boolean isEmptyTask(String taskInput, String operation) {
    String msg = "Enter a valid one";
    if (taskInput.isEmpty()) {
      switch (operation) {
        case "ADD":
          System.out.println("Cant add an empty task " + msg);
          break;
        case "DELETE":
          System.out.println("Cant delete a empty task " + msg);
          break;
        case "COMPLETE":
          System.out.println("Can't complete an empty task " + msg);
          break;
        default:
          System.out.println("Invalid operation");
          break;
      }
      return true;
    }
    return false;
  }

  /**
   * Checks if the task list is empty and prints a message if it's empty
   * 
   * @return true if the task list is empty, false otherwise
   */
  public boolean isEmptyTaskList() {
    if (taskManager.getNoOfTasks() <= 0) {
      System.out.println(emptyTaskListMsg + " Press 1 to add a new task");
      return true;
    }
    return false;
  }

 
  public void start() {
    
    int mainMenuInput = -1; // Store the input for the main menu
    String taskInput; // store the input for the given task description
    System.out.println("Welcome to the Task Management Application");
    // Displaying Main menu options
    System.out.println("Here is the menu options ");
    do {
      System.out.println("\nMENU");
      System.out.println("1.Add a new task ");
      System.out.println("2.Complete an existing task ");
      System.out.println("3.Delete a task ");
      System.out.println("4.View tasks");
      System.out.println("5.Search a task");
      System.out.println("6.Undo");
      System.out.println("0.Exit the application");
      System.out.println("Enter a menu option[0, 1, 2, 3, 4, 5, 6] : ");

      // Using nextLine() instead of nextInt() to avoid input buffering issue
      try {
        mainMenuInput = Integer.parseInt(taskManager.scanner.nextLine());
      } catch (NumberFormatException e) {
        System.err.println("Invalid input. Please enter a number");
        continue;
      }
      switch (mainMenuInput) {
        case MENU_ADD_TASK:
          System.out.println("Enter the task");
          taskInput = taskManager.scanner.nextLine().toUpperCase();
          if (!isEmptyTask(taskInput, "ADD")) {
            taskManager.addTask(taskInput);
          }
          break;

        case MENU_COMPLETE_TASK:
          if (!taskManager.userInput.isEmptyTaskList()) {
            System.out.println("Enter the task ");
            taskInput = taskManager.scanner.nextLine().toUpperCase();
            if (!taskManager.userInput.isEmptyTask(taskInput, "Complete")) {
              taskManager.completeTask(taskInput);
            }
          }
          break;
        case MENU_DELETE_TASK:
          String subMenu1; // Store the sub menu-1 inputs
          // Displaying the submenu-1 options
          do {
            System.out.println("\nSub Menu - delete task");
            System.out.println("3.0 - exit the sub menu");
            System.out.println("3.1 - delete a task");
            System.out.println("3.2 - delete all tasks");
            System.out.println("Enter a sub menu : ");
            subMenu1 = taskManager.scanner.nextLine();
            switch (subMenu1) {
              case SUBMENU_EXIT:
                System.out.println("exited the sub menu ");
                break;
              case SUBMENU_DELETE_TASK:
                if (!taskManager.userInput.isEmptyTaskList()) {
                  System.out.println("Enter the task ");
                  taskInput = taskManager.scanner.nextLine().toUpperCase();
                  if (!taskManager.userInput.isEmptyTask(taskInput, "DELETE")) {
                    taskManager.deleteTask(taskInput);
                  }
                }
                break;
              case SUBMENU_DELETE_ALL_TASKS:
                taskManager.deleteAllTasks();
                break;
              default :
                System.out.println("Enter a valid sub menu");
                break;
            }
          } while (!subMenu1.equals("3.0"));
          break;
        case MENU_VIEW_TASKS:
          String subMenu2; // Store sub menu-2 inputs
          do {
            System.out.println("\nSub Menu - view tasks");
            System.out.println("4.0 - exit the sub menu");
            System.out.println("4.1 - view all tasks");
            System.out.println("4.2 - view completed tasks");
            System.out.println("4.3 - view uncompleted tasks");
            System.out.println("Enter a sub menu ");
            subMenu2 = taskManager.scanner.nextLine();
            switch (subMenu2) {
              case "4.0":
                System.out.println("Exited the sub menu");
                break;
              case SUBMENU_VIEW_ALL_TASKS:
                if (!taskManager.userInput.isEmptyTaskList()) {
                  taskManager.viewAllTasks();
                }
                break;
              case SUBMENU_VIEW_COMPLETED_TASKS:
                if (!taskManager.userInput.isEmptyTaskList()) {
                  taskManager.viewCompletedTasks();
                }
                break;
              case SUBMENU_VIEW_INCOMPLETED_TASKS:
                if (!taskManager.userInput.isEmptyTaskList()) {
                  taskManager.viewIncompletedTasks();
                }
                break;
              default:
                System.out.println("Enter a valid sub menu");
                break;
            }
          } while (!subMenu2.equals("4.0"));
          break;
        case MENU_SEARCH_TASK:
          System.out.println("Enter the task");
          taskManager.searchTask(taskManager.scanner.nextLine().toUpperCase());
          break;
        case MENU_UNDO:
          taskManager.undo();
          break;
        case MENU_EXIT:
          fileManager.writeTasksToDisk();
          System.out.println("\nThanks for visiting.");
          System.out.println("exited the application ");
          break;
        default:
          System.out.println("Enter a valid menu option");
          break;
      }
    } while (mainMenuInput != 0);
    taskManager.scanner.close();
  }
}
