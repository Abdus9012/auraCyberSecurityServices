package com.example.AuraCS.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.AuraCS.model.Task;

@Service
public class TaskService {
    private final List<String> investigators = Arrays.asList("Abdus", "Abhinav", "Shalini", "Roy", "Eswar");
    private final Map<String, Task> taskMap = new ConcurrentHashMap<>();
    private final Map<String, String> investigatorStatus = new ConcurrentHashMap<>();

    //priority queue on the basis of least num of tasks an inv is handling
    private final PriorityQueue<String> investigatorQueue = new PriorityQueue<>(
        Comparator.comparingInt(   //sorts on the least num of the resultant cases handling
           investigator -> (int)taskMap.values()  //value is the num of tasks currently an investigator is handling
           .stream()                              //enables filtering and counting of tasks by serializing all the present tasks of an investigator
           .filter(task -> task.getInvestigator().equals(investigator))     //filters all the tasks associated with the current investigator so that the investigator with least count of cases will be added 1st in the pq.
           .count()
    ));


    //constructor ->initilizes an object whenever created
    public TaskService() {
        investigatorQueue.addAll(investigators);
        investigators.forEach(investigator -> investigatorStatus.put(investigator, "Investigator Availale")); //profound way of iterating and marking as avail
    }

    
    //In core business logic we have 4 functions to be performed:
    //1. assignTask() -> assigns task to investigator on priority basis and puts him into taskMap
    //2. getPendingTasks() -> gets pending unfinished tasks by inv frm taskMap
    //3. completeTask() -> checks if task is completed <=15 mins and marks the apt status
    //4. simulateTime -> if task took >15mins by inv marks the status as "IPH fall" and updates it.
    //5. getinvestigatorStatus() -> returns updated investigator status.


    //Application of Dijkstras algo 
    public synchronized String assignTask(String countryCode, String taskId){
        if(!"India".equalsIgnoreCase(countryCode)) { 
            return "Only tasks for India can be processed"; 
        }

        if(taskMap.isEmpty()){
            return "No investigator is available currently";
        }

        String assignedInvestigator = investigatorQueue.poll();
        Task task = new Task(taskId, assignedInvestigator, 15);
        taskMap.put(assignedInvestigator, task);
        investigatorStatus.put(assignedInvestigator, "Investigator unavailable");
        investigatorQueue.add(assignedInvestigator);

        return "Task assigned to: "+assignedInvestigator;

    }

    public List<Task> getPendingTasks(){
        return new ArrayList<>(taskMap.values());
    }

    public synchronized String completeTask(String taskId){
        Task task = taskMap.get(taskId);
        
        //triggered when an inv completes a task
        //when task is not null and inv completed the task, then that task is removed from taskMap, investigator status is put back to avail and that inv is added back to investigator queue.
        if(task != null){
            taskMap.remove(taskId);
            investigatorStatus.put(task.getInvestigator(), "Investigator Available");
            investigatorQueue.add(task.getInvestigator());

            return taskId + "task is completed by " +task.getInvestigator()+"."; 
        }
        //if task is null => taskMap is empty
        return taskId + " task is not found.";
    }

    public synchronized String simulateTime(){
        for(Task task : taskMap.values()){
            if(task.decrementTime() <= 0){
                investigatorStatus.put(task.getInvestigator(), "IPH Fall");
            }
        }
        return "Simulation complete. Investigator status updated.";
    }
    
    public synchronized Map<String, String> getinvestigatorStatus(){
        return new HashMap<>(investigatorStatus);
    }

}
