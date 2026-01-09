package taskscheduler.model;

import java.io.Serializable;

public class ScheduledTask implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Task task;
    private String day;
    private String startTime;
    private String endTime;
    private boolean isSplit;
    private int splitPart;
    
    public ScheduledTask(Task task, String day, String startTime, String endTime) {
        this.task = task;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSplit = false;
        this.splitPart = 0;
    }
    
    public ScheduledTask(Task task, String day, String startTime, String endTime, int splitPart) {
        this.task = task;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isSplit = true;
        this.splitPart = splitPart;
    }
    
    public Task getTask() { return task; }
    public String getDay() { return day; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public boolean isSplit() { return isSplit; }
    public int getSplitPart() { return splitPart; }
    
    @Override
    public String toString() {
        String splitInfo = isSplit ? " (Part " + splitPart + ")" : "";
        return String.format("  [%s - %s] %s%s - Priority: %s",
                           startTime, endTime, task.getName(), splitInfo, task.getPriority());
    }
}