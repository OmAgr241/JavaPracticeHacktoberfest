import java.io.*;
import java.util.*;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private boolean isCompleted;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return (isCompleted ? "[✔]" : "[ ]") + " " + title + " - " + description;
    }
}

public class TaskManager {
    private static final String FILE_PATH = "tasks.dat";
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadTasks();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Task Manager CLI ===");
            System.out.println("1. View All Tasks");
            System.out.println("2. Add New Task");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. Delete Task");
            System.out.println("5. Clear All Tasks");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = getChoice(sc);

            switch (choice) {
                case 1 -> viewTasks();
                case 2 -> addTask(sc);
                case 3 -> markCompleted(sc);
                case 4 -> deleteTask(sc);
                case 5 -> clearTasks(sc);
                case 6 -> {
                    saveTasks();
                    System.out.println("Exiting... Tasks saved successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static int getChoice(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addTask(Scanner sc) {
        System.out.print("Enter task title: ");
        String title = sc.nextLine();
        System.out.print("Enter task description: ");
        String desc = sc.nextLine();
        tasks.add(new Task(title, desc));
        saveTasks();
        System.out.println("✅ Task added successfully!");
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        System.out.println("\n--- Task List ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void markCompleted(Scanner sc) {
        viewTasks();
        if (tasks.isEmpty()) return;
        System.out.print("Enter task number to mark as completed: ");
        int index = getChoice(sc) - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markCompleted();
            saveTasks();
            System.out.println("✅ Task marked as completed!");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void deleteTask(Scanner sc) {
        viewTasks();
        if (tasks.isEmpty()) return;
        System.out.print("Enter task number to delete: ");
        int index = getChoice(sc) - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveTasks();
            System.out.println("🗑️ Task deleted successfully!");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    private static void clearTasks(Scanner sc) {
        System.out.print("Are you sure you want to clear all tasks? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            tasks.clear();
            saveTasks();
            System.out.println("🔥 All tasks cleared!");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    private static void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            tasks = (ArrayList<Task>) in.readObject();
        } catch (FileNotFoundException e) {
            tasks = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }
}
