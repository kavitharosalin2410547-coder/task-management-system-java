package taskscheduler.model;

import java.io.Serializable;

public class TimeSlot implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    
    public TimeSlot(String startTime, String endTime) {
        parseTime(startTime, true);
        parseTime(endTime, false);
        
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid time slot: end time must be after start time");
        }
    }
    
    private void parseTime(String time, boolean isStart) {
        String[] parts = time.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM");
        }
        
        int hour = Integer.parseInt(parts[0].trim());
        int minute = Integer.parseInt(parts[1].trim());
        
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid time values");
        }
        
        if (isStart) {
            this.startHour = hour;
            this.startMinute = minute;
        } else {
            this.endHour = hour;
            this.endMinute = minute;
        }
    }
    
    private boolean isValid() {
        return (endHour > startHour) || 
               (endHour == startHour && endMinute > startMinute);
    }
    
    public double getDurationHours() {
        int totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
        return totalMinutes / 60.0;
    }
    
    public int getStartHour() { return startHour; }
    public int getStartMinute() { return startMinute; }
    public int getEndHour() { return endHour; }
    public int getEndMinute() { return endMinute; }
    
    @Override
    public String toString() {
        return String.format("%02d:%02d - %02d:%02d (%.2f hours)",
                           startHour, startMinute, endHour, endMinute, getDurationHours());
    }
}