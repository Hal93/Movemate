package com.finalproject.dean.finalprojectapplication;

import java.util.ArrayList;

/**
 * Created by Dean on 01/05/2015.
 */
public class ActivityList {

    private static ActivityList list;
    private ArrayList<String> activityList;

    private ActivityList(){

        activityList = new ArrayList<String>();
    }

    public static ActivityList getInstance(){

        if(list == null){
            list = new ActivityList();
        }

        return list;
    }

    public ArrayList<String> getActivityList(){
        return activityList;
    }
}
