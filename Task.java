package taskscheduler.model;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable, Comparable<Task> {
    private static final long serialVersionUID = 1L;
    
    private String taskId;
    private String name;
    private String description;
    private Priority priority;
    private double durationHours;
    private String deadline;
    private TaskStatus status;
    
    private static int taskCounter = 1;
    
    public Task(String name, String description, Priority priority, 
                double durationHours, String deadline) {
        this.taskId = "T" + String.format("%03d", taskCounter++);
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.durationHours = durationHours;
        this.deadline = deadline;
        this.status = TaskStatus.PENDING;
    }
    
    public String getTaskId() { return taskId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public double getDurationHours() { return durationHours; }
    public String getDeadline() { return deadline; }
    public TaskStatus getStatus() { return status; }
    
    public void setStatus(TaskStatus status) { this.status = status; }
    
    @Override
    public int compareTo(Task other) {
        int priorityCompare = this.priority.compareTo(other.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return this.deadline.compareTo(other.deadline);
    }
    
    public String toTableFormat() {
        return String.format("%-8s %-25s %-10s %-10.2f %-12s %-10s",
                           taskId, 
                           name.length() > 25 ? name.substring(0, 22) + "..." : name,
                           priority,
                           durationHours,
                           deadline,
                           status);
    }
    
    @Override
    public String toString() {
        return String.format("Task[%s]: %s | Priority: %s | Duration: %.2fh | Deadline: %s | Status: %s",
                           taskId, name, priority, durationHours, deadline, status);
    }
}