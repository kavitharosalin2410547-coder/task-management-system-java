package taskscheduler.service;

import taskscheduler.model.TimeSlot;
import java.io.Serializable;
import java.util.*;

public class UserScheduleManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<TimeSlot> weekdaySchedule;
    private List<TimeSlot> weekendSchedule;
    
    public UserScheduleManager() {
        this.weekdaySchedule = new ArrayList<>();
        this.weekendSchedule = new ArrayList<>();
    }
    
    public void setWeekdaySchedule(List<TimeSlot> schedule) {
        this.weekdaySchedule = new ArrayList<>(schedule);
    }
    
    public void setWeekendSchedule(List<TimeSlot> schedule) {
        this.weekendSchedule = new ArrayList<>(schedule);
    }
    
    public List<TimeSlot> getWeekdaySchedule() {
        return new ArrayList<>(weekdaySchedule);
    }
    
    public List<TimeSlot> getWeekendSchedule() {
        return new ArrayList<>(weekendSchedule);
    }
    
    public boolean isConfigured() {
        return !weekdaySchedule.isEmpty() || !weekendSchedule.isEmpty();
    }
    
    public double getTotalWeeklyHours() {
        double weekdayHours = weekdaySchedule.stream()
                                            .mapToDouble(TimeSlot::getDurationHours)
                                            .sum() * 5;
        double weekendHours = weekendSchedule.stream()
                                            .mapToDouble(TimeSlot::getDurationHours)
                                            .sum() * 2;
        return weekdayHours + weekendHours;
    }
}
