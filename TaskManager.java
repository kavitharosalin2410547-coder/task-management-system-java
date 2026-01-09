package taskscheduler.service;

import taskscheduler.model.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Task> tasks;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    public List<Task> getPendingTasks() {
        return tasks.stream()
                   .filter(t -> t.getStatus() == TaskStatus.PENDING)
                   .sorted()
                   .collect(Collectors.toList());
    }
    
    public Task getTaskById(String taskId) {
        return tasks.stream()
                   .filter(t -> t.getTaskId().equals(taskId))
                   .findFirst()
                   .orElse(null);
    }
    
    public boolean markTaskComplete(String taskId) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(TaskStatus.COMPLETED);
            return true;
        }
        return false;
    }
    
    public boolean deleteTask(String taskId) {
        return tasks.removeIf(t -> t.getTaskId().equals(taskId));
    }
    
    public Map<String, Integer> getTaskStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", tasks.size());
        stats.put("pending", (int) tasks.stream()
                                         .filter(t -> t.getStatus() == TaskStatus.PENDING)
                                         .count());
        stats.put("completed", (int) tasks.stream()
                                          .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                                          .count());
        return stats;
    }
}
