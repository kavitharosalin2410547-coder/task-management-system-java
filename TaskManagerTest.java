package taskscheduler.test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import taskscheduler.model.*;
import taskscheduler.service.TaskManager;
import java.util.List;

/**
 * JUnit test cases for TaskManager class
 */
public class TaskManagerTest {
    private TaskManager taskManager;
    private Task testTask1;
    private Task testTask2;
    
    @BeforeEach
    public void setUp() {
        taskManager = new TaskManager();
        testTask1 = new Task("Study Java", "Complete OOP concepts", 
                            Priority.HIGH, 2.0, "30-10-2025");
        testTask2 = new Task("Exercise", "Morning workout", 
                            Priority.MEDIUM, 1.0, "25-10-2025");
    }
    
    @Test
    @DisplayName("Test adding a task")
    public void testAddTask() {
        taskManager.addTask(testTask1);
        List<Task> tasks = taskManager.getAllTasks();
        
        assertEquals(1, tasks.size(), "Should have 1 task");
        assertEquals("Study Java", tasks.get(0).getName());
    }
    
    @Test
    @DisplayName("Test adding multiple tasks")
    public void testAddMultipleTasks() {
        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);
        
        assertEquals(2, taskManager.getAllTasks().size(), "Should have 2 tasks");
    }
    
    @Test
    @DisplayName("Test getting pending tasks")
    public void testGetPendingTasks() {
        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);
        
        List<Task> pending = taskManager.getPendingTasks();
        assertEquals(2, pending.size(), "Should have 2 pending tasks");
        
        taskManager.markTaskComplete(testTask1.getTaskId());
        pending = taskManager.getPendingTasks();
        assertEquals(1, pending.size(), "Should have 1 pending task after completion");
    }
    
    @Test
    @DisplayName("Test marking task as complete")
    public void testMarkTaskComplete() {
        taskManager.addTask(testTask1);
        String taskId = testTask1.getTaskId();
        
        boolean result = taskManager.markTaskComplete(taskId);
        assertTrue(result, "Should return true for valid task ID");
        assertEquals(TaskStatus.COMPLETED, testTask1.getStatus());
    }
    
    @Test
    @DisplayName("Test marking non-existent task as complete")
    public void testMarkNonExistentTaskComplete() {
        boolean result = taskManager.markTaskComplete("INVALID_ID");
        assertFalse(result, "Should return false for invalid task ID");
    }
    
    @Test
    @DisplayName("Test deleting a task")
    public void testDeleteTask() {
        taskManager.addTask(testTask1);
        String taskId = testTask1.getTaskId();
        
        boolean result = taskManager.deleteTask(taskId);
        assertTrue(result, "Should return true for successful deletion");
        assertEquals(0, taskManager.getAllTasks().size(), "Task list should be empty");
    }
    
    @Test
    @DisplayName("Test deleting non-existent task")
    public void testDeleteNonExistentTask() {
        boolean result = taskManager.deleteTask("INVALID_ID");
        assertFalse(result, "Should return false for invalid task ID");
    }
    
    @Test
    @DisplayName("Test get task by ID")
    public void testGetTaskById() {
        taskManager.addTask(testTask1);
        String taskId = testTask1.getTaskId();
        
        Task found = taskManager.getTaskById(taskId);
        assertNotNull(found, "Task should be found");
        assertEquals("Study Java", found.getName());
    }
    
    @Test
    @DisplayName("Test task statistics")
    public void testGetTaskStatistics() {
        taskManager.addTask(testTask1);
        taskManager.addTask(testTask2);
        taskManager.markTaskComplete(testTask1.getTaskId());
        
        var stats = taskManager.getTaskStatistics();
        assertEquals(2, stats.get("total"), "Total should be 2");
        assertEquals(1, stats.get("completed"), "Completed should be 1");
        assertEquals(1, stats.get("pending"), "Pending should be 1");
    }
    
    @Test
    @DisplayName("Test task priority sorting")
    public void testTaskPrioritySorting() {
        Task lowPriorityTask = new Task("Low Task", "Low priority", 
                                       Priority.LOW, 1.0, "28-10-2025");
        Task highPriorityTask = new Task("High Task", "High priority", 
                                        Priority.HIGH, 1.0, "28-10-2025");
        
        taskManager.addTask(lowPriorityTask);
        taskManager.addTask(highPriorityTask);
        
        List<Task> pending = taskManager.getPendingTasks();
        assertEquals(Priority.HIGH, pending.get(0).getPriority(), 
                    "First task should be high priority");
    }
}
