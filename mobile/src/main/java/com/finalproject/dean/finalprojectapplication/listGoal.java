package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dean on 23/03/2015.
 */
public class listGoal extends Activity {

    LoginDataBaseAdapter db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listgoal);

        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        ArrayList<String> goalList = new ArrayList<String>();
        goalList = db.getAllGoals();
        for(int i=0; i<=goalList.size(); i++){

        }

        //instantiate custom adapter
        MyCustomGoalAdapter adapter = new MyCustomGoalAdapter(goalList, this);

        //handle listview and assign adapter
        ListView goalView = (ListView)findViewById(R.id.goalList);
        goalView.setAdapter(adapter);
    }

    // Methods to handleClick Event of Sign In Button
    public void clickBckButton(View V)
    {
        Intent intentSettingsScreen=new Intent(getApplicationContext(),monitor.class);
        startActivity(intentSettingsScreen);
    }


}


