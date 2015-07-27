package com.finalproject.dean.finalprojectapplication;

/**
 * Created by Dean on 01/04/2015.
 */
public class Goals {
    int id;
    String goalName;
    String goalTime;
    String goalType;

    // constructors
    public Goals() {
    }

    public Goals(String goalName, String goalTime, String goalType) {
        this.goalName = goalName;
        this.goalTime = goalTime;
        this.goalType = goalType;
    }

    public Goals(int id, String goalName, String goalTime, String goalType) {
        this.id = id;
        this.goalName = goalName;
        this.goalTime = goalTime;
        this.goalType = goalType;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setGoalTime(String goalTime) {
        this.goalType = goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getGoalName() {
        return this.goalName;
    }

    public String getGoalTime() {
        return this.goalTime;
    }

    public String getGoalType() {
        return this.goalType;
    }
}
