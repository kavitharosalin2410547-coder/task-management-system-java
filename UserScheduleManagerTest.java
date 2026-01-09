package taskscheduler.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import taskscheduler.model.TimeSlot;
import taskscheduler.service.UserScheduleManager;
import java.util.*;

/**
 * JUnit test cases for UserScheduleManager class
 */
public class UserScheduleManagerTest {
    private UserScheduleManager scheduleManager;
    private List<TimeSlot> testWeekdaySlots;
    private List<TimeSlot> testWeekendSlots;
    
    @BeforeEach
    public void setUp() {
        scheduleManager = new UserScheduleManager();
        
        testWeekdaySlots = new ArrayList<>();
        testWeekdaySlots.add(new TimeSlot("09:00", "12:00"));
        testWeekdaySlots.add(new TimeSlot("14:00", "17:00"));
        
        testWeekendSlots = new ArrayList<>();
        testWeekendSlots.add(new TimeSlot("10:00", "16:00"));
    }
    
    @Test
    @DisplayName("Test setting weekday schedule")
    public void testSetWeekdaySchedule() {
        scheduleManager.setWeekdaySchedule(testWeekdaySlots);
        List<TimeSlot> retrieved = scheduleManager.getWeekdaySchedule();
        
        assertEquals(2, retrieved.size(), "Should have 2 weekday slots");
    }
    
    @Test
    @DisplayName("Test setting weekend schedule")
    public void testSetWeekendSchedule() {
        scheduleManager.setWeekendSchedule(testWeekendSlots);
        List<TimeSlot> retrieved = scheduleManager.getWeekendSchedule();
        
        assertEquals(1, retrieved.size(), "Should have 1 weekend slot");
    }
    
    @Test
    @DisplayName("Test schedule configuration status")
    public void testIsConfigured() {
        assertFalse(scheduleManager.isConfigured(), "Should not be configured initially");
        
        scheduleManager.setWeekdaySchedule(testWeekdaySlots);
        assertTrue(scheduleManager.isConfigured(), "Should be configured after setting weekday");
    }
    
    @Test
    @DisplayName("Test total weekly hours calculation")
    public void testGetTotalWeeklyHours() {
        scheduleManager.setWeekdaySchedule(testWeekdaySlots);
        scheduleManager.setWeekendSchedule(testWeekendSlots);
        
        // Weekday: (3 + 3) * 5 = 30 hours
        // Weekend: 6 * 2 = 12 hours
        // Total: 42 hours
        double totalHours = scheduleManager.getTotalWeeklyHours();
        assertEquals(42.0, totalHours, 0.01, "Total weekly hours should be 42");
    }
    
    @Test
    @DisplayName("Test empty schedule total hours")
    public void testEmptyScheduleTotalHours() {
        double totalHours = scheduleManager.getTotalWeeklyHours();
        assertEquals(0.0, totalHours, "Empty schedule should have 0 hours");
    }
}