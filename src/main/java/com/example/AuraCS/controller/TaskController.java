package com.example.AuraCS.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AuraCS.model.Task;
import com.example.AuraCS.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    //3 functions
    //1. @PostMapping -> assignTask()
    //2. @GetMapping -> getPendingTask(), getInvestigatorStatus(),  simulateTime()
    //3. @DeleteMapping() -> completeTask()

     @PostMapping("/assign")
      public String assignTask(@RequestParam String countryCode, @RequestParam String taskId){
            return taskService.assignTask(countryCode, taskId);
        }
    
    @GetMapping("/status")
    public List<Task> getPendingTasks(){
        return taskService.getPendingTasks();
    }

    @GetMapping("/investigator-status")
    public Map<String, String> getInvestigatorStatus(){
        return taskService.getinvestigatorStatus();
    }

    @GetMapping("/simulate")
    public String simulateTime(){
        return taskService.simulateTime();
    }

    @DeleteMapping("/status")
    public String completeTask(@RequestParam String taskId){
        return taskService.completeTask(taskId);
    }
}
