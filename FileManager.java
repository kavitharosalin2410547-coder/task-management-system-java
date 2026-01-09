package taskscheduler.util;

import taskscheduler.service.*;
import java.io.*;

public class FileManager {
    private static final String DATA_DIR = "data/";
    private static final String SCHEDULE_FILE = DATA_DIR + "user_schedule.dat";
    private static final String TASKS_FILE = DATA_DIR + "tasks.dat";
    private static final String GENERATED_SCHEDULE_FILE = DATA_DIR + "generated_schedule.dat";
    
    public FileManager() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    public void saveUserSchedule(UserScheduleManager scheduleManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SCHEDULE_FILE))) {
            oos.writeObject(scheduleManager);
        } catch (IOException e) {
            System.err.println("Error saving user schedule: " + e.getMessage());
        }
    }
    
    public UserScheduleManager loadUserSchedule() {
        File file = new File(SCHEDULE_FILE);
        if (!file.exists()) {
            return new UserScheduleManager();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SCHEDULE_FILE))) {
            return (UserScheduleManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user schedule: " + e.getMessage());
            return new UserScheduleManager();
        }
    }
    
    public void saveTasks(TaskManager taskManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TASKS_FILE))) {
            oos.writeObject(taskManager);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    public TaskManager loadTasks() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            return new TaskManager();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TASKS_FILE))) {
            return (TaskManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            return new TaskManager();
        }
    }
    
    public void saveSchedule(SchedulingEngine schedulingEngine) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(GENERATED_SCHEDULE_FILE))) {
            oos.writeObject(schedulingEngine);
        } catch (IOException e) {
            System.err.println("Error saving schedule: " + e.getMessage());
        }
    }
    
    public SchedulingEngine loadSchedule() {
        File file = new File(GENERATED_SCHEDULE_FILE);
        if (!file.exists()) {
            return new SchedulingEngine();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(GENERATED_SCHEDULE_FILE))) {
            return (SchedulingEngine) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading schedule: " + e.getMessage());
            return new SchedulingEngine();
        }
    }
    
    public void exportScheduleToText(SchedulingEngine schedulingEngine, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            writer.println("═══════════════════════════════════════════════════════");
            writer.println("           CUSTOMIZABLE TASK SCHEDULER - EXPORT         ");
            writer.println("═══════════════════════════════════════════════════════\n");
            writer.println("Generated on: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            writer.println();
            
            var schedule = schedulingEngine.getSchedule();
            
            for (var entry : schedule.entrySet()) {
                writer.println("\n>>> " + entry.getKey().toUpperCase() + " <<<");
                writer.println("────────────────────────────────────────────────────────────────");
                
                if (entry.getValue().isEmpty()) {
                    writer.println("  No tasks scheduled for this day.");
                } else {
                    for (var st : entry.getValue()) {
                        writer.println(st);
                    }
                }
            }
            
            var unscheduledTasks = schedulingEngine.getUnscheduledTasks();
            if (!unscheduledTasks.isEmpty()) {
                writer.println("\n>>> UNSCHEDULED TASKS <<<");
                writer.println("────────────────────────────────────────────────────────────────");
                for (var task : unscheduledTasks) {
                    writer.println("  [" + task.getTaskId() + "] " + task.getName() + 
                                 " - Priority: " + task.getPriority() + 
                                 " - Duration: " + task.getDurationHours() + "h");
                }
            }
            
            writer.println("\n═══════════════════════════════════════════════════════");
            
        } catch (IOException e) {
            System.err.println("Error exporting schedule: " + e.getMessage());
        }
    }
    
    public void clearAllData() {
        deleteFile(SCHEDULE_FILE);
        deleteFile(TASKS_FILE);
        deleteFile(GENERATED_SCHEDULE_FILE);
        System.out.println("All data files cleared.");
    }
    
    private void deleteFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            file.delete();
        }
    }
}