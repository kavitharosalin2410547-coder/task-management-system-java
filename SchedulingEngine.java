package taskscheduler.test;

import taskscheduler.model.*;
import java.io.Serializable;
import java.util.*;

/**
 * Core scheduling engine that allocates tasks to time slots
 * Ensures optimal spacing to avoid cramming
 */
public class SchedulingEngine implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double BUFFER_TIME = 0.25;
    
    private Map<String, List<ScheduledTask>> schedule;
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", 
                                         "Thursday", "Friday", "Saturday", "Sunday"};
    
    public SchedulingEngine() {
        this.schedule = new LinkedHashMap<>();
        initializeSchedule();
    }
    
    /**
     * Initializes empty schedule for all days
     */
    private void initializeSchedule() {
        for (String day : DAYS) {
            schedule.put(day, new ArrayList<>());
        }
    }
    
    /**
     * Generates optimized schedule for given tasks
     */
    public boolean generateSchedule(List<Task> tasks, 
                                   List<TimeSlot> weekdaySlots, 
                                   List<TimeSlot> weekendSlots) {
        initializeSchedule();
        
        Queue<Task> taskQueue = new PriorityQueue<>(tasks);
        
        int dayIndex = 0;
        int slotIndex = 0;
        double currentSlotTimeUsed = 0;
        
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            double taskDuration = task.getDurationHours() + BUFFER_TIME;
            
            boolean scheduled = false;
            int attempts = 0;
            
            while (!scheduled && attempts < 14) {
                String currentDay = DAYS[dayIndex % 7];
                List<TimeSlot> availableSlots = isWeekend(currentDay) ? 
                                                weekendSlots : weekdaySlots;
                
                if (availableSlots.isEmpty()) {
                    dayIndex++;
                    attempts++;
                    slotIndex = 0;
                    currentSlotTimeUsed = 0;
                    continue;
                }
                
                if (slotIndex >= availableSlots.size()) {
                    dayIndex++;
                    attempts++;
                    slotIndex = 0;
                    currentSlotTimeUsed = 0;
                    continue;
                }
                
                TimeSlot currentSlot = availableSlots.get(slotIndex);
                double remainingSlotTime = currentSlot.getDurationHours() - currentSlotTimeUsed;
                
                if (taskDuration <= remainingSlotTime) {
                    String startTime = calculateTime(currentSlot.getStartHour(), 
                                                    currentSlot.getStartMinute(), 
                                                    currentSlotTimeUsed);
                    String endTime = calculateTime(currentSlot.getStartHour(), 
                                                  currentSlot.getStartMinute(), 
                                                  currentSlotTimeUsed + task.getDurationHours());
                    
                    ScheduledTask st = new ScheduledTask(task, currentDay, startTime, endTime);
                    schedule.get(currentDay).add(st);
                    
                    currentSlotTimeUsed += taskDuration;
                    scheduled = true;
                } else {
                    slotIndex++;
                    currentSlotTimeUsed = 0;
                    
                    if (slotIndex >= availableSlots.size()) {
                        dayIndex++;
                        slotIndex = 0;
                    }
                }
                
                attempts++;
            }
            
            if (!scheduled) {
                System.out.println("Warning: Could not schedule task: " + task.getName());
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Calculates time string after adding hours
     */
    private String calculateTime(int startHour, int startMinute, double hoursToAdd) {
        int totalMinutes = (int)(hoursToAdd * 60);
        int hour = startHour + (startMinute + totalMinutes) / 60;
        int minute = (startMinute + totalMinutes) % 60;
        return String.format("%02d:%02d", hour, minute);
    }
    
    /**
     * Checks if a day is weekend
     */
    private boolean isWeekend(String day) {
        return day.equals("Saturday") || day.equals("Sunday");
    }
    
    /**
     * Returns the generated schedule
     */
    public Map<String, List<ScheduledTask>> getSchedule() {
        return new LinkedHashMap<>(schedule);
    }
    
    /**
     * Removes a task from the schedule
     */
    public void removeTaskFromSchedule(String taskId) {
        for (List<ScheduledTask> dayTasks : schedule.values()) {
            dayTasks.removeIf(st -> st.getTask().getTaskId().equals(taskId));
        }
    }
    
    /**
     * Clears the entire schedule
     */
    public void clearSchedule() {
        initializeSchedule();
    }
}