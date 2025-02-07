package com.example.AuraCS.model;

public class Task {

    private String taskId;
    private String investigator;
    private int timeRemaining;
    public Task(String taskId, String investigator, int timeRemaining) {
        this.taskId = taskId;
        this.investigator = investigator;
        this.timeRemaining = timeRemaining;
    }
    public String getTaskId() {
        return taskId;
    }
    public String getInvestigator() {
        return investigator;
    }
    public int getTimeRemaining() {
        return timeRemaining;
    }
    public int decrementTime(){
        return --timeRemaining;
    }

}
