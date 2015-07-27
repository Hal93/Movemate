package com.finalproject.dean.finalprojectapplication;

import android.app.Application;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Dean on 04/05/2015.
 */
public class MyApplication extends Application{

    static ArrayList<String> commonList = new ArrayList<String>();
    static Timer timer = new Timer();

    public void cancelTimer(){
        timer.cancel();
    }

    public Timer getTimer(){
        return timer;
    }

    public ArrayList<String> getList()
    {
        return commonList;
    }

    public void addtoList(String item)
    {
        commonList.add(item);
    }

    public int getArraySize(){
        int size = commonList.size();
        return size;
    }

}