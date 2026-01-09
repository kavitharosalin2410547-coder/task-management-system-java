package taskscheduler;

import taskscheduler.model.*;
import taskscheduler.service.*;
import taskscheduler.util.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserScheduleManager scheduleManager = new UserScheduleManager();
    private static TaskManager taskManager = new TaskManager();
    private static SchedulingEngine schedulingEngine = new SchedulingEngine();
    private static FileManager fileManager = new FileManager();
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("    CUSTOMIZABLE TASK SCHEDULER - TO-DO LIST SYSTEM    ");
        System.out.println("═══════════════════════════════════════════════════════\n");
        
        loadData();
        
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    configureUserSchedule();
                    break;
                case 2:
                    addNewTask();
                    break;
                case 3:
                    viewAllTasks();
                    break;
                case 4:
                    generateSchedule();
                    break;
                case 5:
                    viewGeneratedSchedule();
                    break;
                case 6:
                    markTaskComplete();
                    break;
                case 7:
                    deleteTask();
                    break;
                case 8:
                    viewUserSchedule();
                    break;
                case 9:
                    editUserSchedule();
                    break;
                case 10:
                    exportScheduleToFile();
                    break;
                case 11:
                    saveData();
                    System.out.println("\n✓ Data saved successfully!");
                    System.out.println("Thank you for using Task Scheduler. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("\n✗ Invalid choice! Please try again.\n");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n─────────────────── MAIN MENU ───────────────────");
        System.out.println("1. Configure Your Daily Schedule");
        System.out.println("2. Add New Task");
        System.out.println("3. View All Tasks");
        System.out.println("4. Generate Optimized Schedule");
        System.out.println("5. View Generated Schedule");
        System.out.println("6. Mark Task as Complete");
        System.out.println("7. Delete Task");
        System.out.println("8. View Your Availability Schedule");
        System.out.println("9. Edit Your Availability Schedule");
        System.out.println("10. Export Schedule to File");
        System.out.println("11. Save & Exit");
        System.out.println("─────────────────────────────────────────────────\n");
    }
    
    private static void configureUserSchedule() {
        System.out.println("\n═══════ CONFIGURE YOUR DAILY SCHEDULE ═══════");
        System.out.println("Enter your available time slots for working on tasks.\n");
        
        System.out.println(">>> WEEKDAY SCHEDULE (Monday - Friday) <<<");
        List<TimeSlot> weekdaySlots = getTimeSlots();
        scheduleManager.setWeekdaySchedule(weekdaySlots);
        
        System.out.println("\n>>> WEEKEND SCHEDULE (Saturday - Sunday) <<<");
        List<TimeSlot> weekendSlots = getTimeSlots();
        scheduleManager.setWeekendSchedule(weekendSlots);
        
        System.out.println("\n✓ Your schedule has been configured successfully!");
    }
    
    private static List<TimeSlot> getTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        int numSlots = getIntInput("How many time slots do you have available: ");
        
        for (int i = 1; i <= numSlots; i++) {
            System.out.println("\nTime Slot " + i + ":");
            System.out.print("  Start time (HH:MM in 24-hour format): ");
            String startTime = scanner.nextLine();
            System.out.print("  End time (HH:MM in 24-hour format): ");
            String endTime = scanner.nextLine();
            
            try {
                TimeSlot slot = new TimeSlot(startTime, endTime);
                slots.add(slot);
                System.out.println("  ✓ Added: " + slot);
            } catch (IllegalArgumentException e) {
                System.out.println("  ✗ Invalid time format! Skipping this slot.");
                i--;
            }
        }
        
        return slots;
    }
    
    private static void addNewTask() {
        System.out.println("\n═══════ ADD NEW TASK ═══════");
        
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter task priority (HIGH/MEDIUM/LOW): ");
        String priorityStr = scanner.nextLine().toUpperCase();
        Priority priority;
        try {
            priority = Priority.valueOf(priorityStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority! Setting to MEDIUM.");
            priority = Priority.MEDIUM;
        }
        
        double duration = getDoubleInput("Enter estimated duration (in hours): ");
        
        System.out.print("Enter deadline (DD-MM-YYYY): ");
        String deadline = scanner.nextLine();
        
        Task task = new Task(name, description, priority, duration, deadline);
        taskManager.addTask(task);
        
        System.out.println("\n✓ Task added successfully with ID: " + task.getTaskId());
    }
    
    private static void viewAllTasks() {
        System.out.println("\n═══════ ALL TASKS ═══════");
        List<Task> tasks = taskManager.getAllTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks found. Add some tasks first!");
            return;
        }
        
        System.out.println("\n" + String.format("%-8s %-25s %-10s %-10s %-12s %-10s", 
                         "ID", "Name", "Priority", "Duration", "Deadline", "Status"));
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────");
        
        for (Task task : tasks) {
            System.out.println(task.toTableFormat());
        }
    }
    
    private static void generateSchedule() {
        System.out.println("\n═══════ GENERATING OPTIMIZED SCHEDULE ═══════");
        
        if (!scheduleManager.isConfigured()) {
            System.out.println("✗ Please configure your daily schedule first (Option 1)!");
            return;
        }
        
        List<Task> pendingTasks = taskManager.getPendingTasks();
        if (pendingTasks.isEmpty()) {
            System.out.println("✗ No pending tasks to schedule!");
            return;
        }
        
        System.out.println("\nFound " + pendingTasks.size() + " pending task(s).");
        System.out.println("Scheduling with optimal spacing to avoid cramming...");
        System.out.println("Tasks can be split across multiple time slots if needed.\n");
        
        boolean success = schedulingEngine.generateSchedule(
            pendingTasks, 
            scheduleManager.getWeekdaySchedule(), 
            scheduleManager.getWeekendSchedule()
        );
        
        if (success) {
            System.out.println("\n✓ All tasks scheduled successfully!");
            System.out.println("  View it using Option 5 from the main menu.");
        } else {
            System.out.println("\n⚠ Schedule generated with some limitations.");
            System.out.println("  Some low-priority tasks were not scheduled due to time constraints.");
            System.out.println("  View details using Option 5 from the main menu.");
        }
    }
    
    private static void viewGeneratedSchedule() {
        System.out.println("\n═══════ YOUR OPTIMIZED SCHEDULE ═══════");
        
        Map<String, List<ScheduledTask>> schedule = schedulingEngine.getSchedule();
        
        if (schedule.isEmpty()) {
            System.out.println("No schedule generated yet. Use Option 4 to generate one!");
            return;
        }
        
        for (Map.Entry<String, List<ScheduledTask>> entry : schedule.entrySet()) {
            System.out.println("\n>>> " + entry.getKey().toUpperCase() + " <<<");
            System.out.println("────────────────────────────────────────────────────────────────────────────────");
            
            if (entry.getValue().isEmpty()) {
                System.out.println("  No tasks scheduled for this day.");
            } else {
                for (ScheduledTask st : entry.getValue()) {
                    System.out.println(st);
                }
            }
        }
        
        List<Task> unscheduled = schedulingEngine.getUnscheduledTasks();
        if (!unscheduled.isEmpty()) {
            System.out.println("\n>>> UNSCHEDULED TASKS <<<");
            System.out.println("────────────────────────────────────────────────────────────────────────────────");
            for (Task task : unscheduled) {
                System.out.println("  [" + task.getTaskId() + "] " + task.getName() + 
                                 " - Priority: " + task.getPriority() + 
                                 " - Duration: " + task.getDurationHours() + "h");
            }
            System.out.println("\n  ⚠ These tasks need more time or lower priority prevented scheduling.");
        }
    }
    
    private static void markTaskComplete() {
        System.out.println("\n═══════ MARK TASK AS COMPLETE ═══════");
        
        List<Task> pendingTasks = taskManager.getPendingTasks();
        
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks found!");
            return;
        }
        
        System.out.println("\nPending Tasks:");
        System.out.println(String.format("%-8s %-25s %-10s %-10s %-12s", 
                         "ID", "Name", "Priority", "Duration", "Deadline"));
        System.out.println("─────────────────────────────────────────────────────────────────────");
        
        for (Task task : pendingTasks) {
            System.out.println(String.format("%-8s %-25s %-10s %-10.2f %-12s",
                           task.getTaskId(), 
                           task.getName().length() > 25 ? task.getName().substring(0, 22) + "..." : task.getName(),
                           task.getPriority(),
                           task.getDurationHours(),
                           task.getDeadline()));
        }
        
        String taskId = getStringInput("\nEnter Task ID to mark as complete: ");
        
        if (taskManager.markTaskComplete(taskId)) {
            System.out.println("✓ Task marked as complete!");
            schedulingEngine.removeTaskFromSchedule(taskId);
            
            System.out.println("\nUpdated Pending Tasks: " + taskManager.getPendingTasks().size());
        } else {
            System.out.println("✗ Task not found!");
        }
    }
    
    private static void deleteTask() {
        System.out.println("\n═══════ DELETE TASK ═══════");
        viewAllTasks();
        
        String taskId = getStringInput("\nEnter Task ID to delete: ");
        
        if (taskManager.deleteTask(taskId)) {
            System.out.println("✓ Task deleted successfully!");
            schedulingEngine.removeTaskFromSchedule(taskId);
        } else {
            System.out.println("✗ Task not found!");
        }
    }
    
    private static void viewUserSchedule() {
        System.out.println("\n═══════ YOUR AVAILABILITY SCHEDULE ═══════");
        
        if (!scheduleManager.isConfigured()) {
            System.out.println("Schedule not configured yet. Use Option 1 to configure!");
            return;
        }
        
        System.out.println("\n>>> WEEKDAYS (Monday - Friday) <<<");
        for (TimeSlot slot : scheduleManager.getWeekdaySchedule()) {
            System.out.println("  " + slot);
        }
        
        System.out.println("\n>>> WEEKENDS (Saturday - Sunday) <<<");
        for (TimeSlot slot : scheduleManager.getWeekendSchedule()) {
            System.out.println("  " + slot);
        }
    }
    
    private static void editUserSchedule() {
        System.out.println("\n═══════ EDIT YOUR AVAILABILITY SCHEDULE ═══════");
        
        if (!scheduleManager.isConfigured()) {
            System.out.println("No schedule configured yet. Use Option 1 first!");
            return;
        }
        
        System.out.println("What would you like to edit:");
        System.out.println("1. Edit Weekday Schedule");
        System.out.println("2. Edit Weekend Schedule");
        System.out.println("3. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.println("\n>>> CURRENT WEEKDAY SCHEDULE <<<");
                for (TimeSlot slot : scheduleManager.getWeekdaySchedule()) {
                    System.out.println("  " + slot);
                }
                System.out.println("\n>>> ENTER NEW WEEKDAY SCHEDULE <<<");
                List<TimeSlot> newWeekdaySlots = getTimeSlots();
                scheduleManager.setWeekdaySchedule(newWeekdaySlots);
                System.out.println("✓ Weekday schedule updated!");
                
                System.out.print("\nRegenerate schedule with new availability (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    generateSchedule();
                }
                break;
                
            case 2:
                System.out.println("\n>>> CURRENT WEEKEND SCHEDULE <<<");
                for (TimeSlot slot : scheduleManager.getWeekendSchedule()) {
                    System.out.println("  " + slot);
                }
                System.out.println("\n>>> ENTER NEW WEEKEND SCHEDULE <<<");
                List<TimeSlot> newWeekendSlots = getTimeSlots();
                scheduleManager.setWeekendSchedule(newWeekendSlots);
                System.out.println("✓ Weekend schedule updated!");
                
                System.out.print("\nRegenerate schedule with new availability (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    generateSchedule();
                }
                break;
                
            case 3:
                System.out.println("Edit cancelled.");
                break;
                
            default:
                System.out.println("Invalid choice!");
        }
    }
    
    private static void exportScheduleToFile() {
        System.out.println("\n═══════ EXPORT SCHEDULE TO FILE ═══════");
        
        Map<String, List<ScheduledTask>> schedule = schedulingEngine.getSchedule();
        
        if (schedule.isEmpty() || schedule.values().stream().allMatch(List::isEmpty)) {
            System.out.println("No schedule to export. Generate a schedule first (Option 4)!");
            return;
        }
        
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = "schedule_" + System.currentTimeMillis();
        }
        filename = filename + ".txt";
        
        fileManager.exportScheduleToText(schedulingEngine, filename);
        System.out.println("✓ Schedule exported successfully to data/" + filename);
    }
    
    private static void loadData() {
        System.out.println("Loading saved data...");
        scheduleManager = fileManager.loadUserSchedule();
        taskManager = fileManager.loadTasks();
        schedulingEngine = fileManager.loadSchedule();
        System.out.println("✓ Data loaded successfully!\n");
    }
    
    private static void saveData() {
        System.out.println("\nSaving data...");
        fileManager.saveUserSchedule(scheduleManager);
        fileManager.saveTasks(taskManager);
        fileManager.saveSchedule(schedulingEngine);
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("✗ Please enter a valid number!");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value <= 0) {
                    System.out.println("✗ Please enter a positive number!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("✗ Please enter a valid number!");
            }
        }
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}