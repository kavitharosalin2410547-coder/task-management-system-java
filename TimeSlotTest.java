package taskscheduler.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import taskscheduler.model.TimeSlot;

/**
 * JUnit test cases for TimeSlot class
 */
public class TimeSlotTest {
    
    @Test
    @DisplayName("Test valid time slot creation")
    public void testValidTimeSlot() {
        TimeSlot slot = new TimeSlot("09:00", "12:00");
        assertNotNull(slot, "TimeSlot should be created");
        assertEquals(9, slot.getStartHour());
        assertEquals(0, slot.getStartMinute());
        assertEquals(12, slot.getEndHour());
        assertEquals(0, slot.getEndMinute());
    }
    
    @Test
    @DisplayName("Test time slot duration calculation")
    public void testDurationCalculation() {
        TimeSlot slot = new TimeSlot("09:00", "12:30");
        assertEquals(3.5, slot.getDurationHours(), 0.01, "Duration should be 3.5 hours");
    }
    
    @Test
    @DisplayName("Test invalid time slot - end before start")
    public void testInvalidTimeSlot() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot("12:00", "09:00");
        }, "Should throw exception for end time before start time");
    }
    
    @Test
    @DisplayName("Test invalid time format")
    public void testInvalidTimeFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot("25:00", "26:00");
        }, "Should throw exception for invalid hour");
    }
    
    @Test
    @DisplayName("Test time slot with same start and end")
    public void testSameStartEnd() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TimeSlot("10:00", "10:00");
        }, "Should throw exception for same start and end time");
    }
    
    @Test
    @DisplayName("Test time slot toString")
    public void testToString() {
        TimeSlot slot = new TimeSlot("14:30", "17:00");
        String str = slot.toString();
        assertTrue(str.contains("14:30"), "Should contain start time");
        assertTrue(str.contains("17:00"), "Should contain end time");
    }
}