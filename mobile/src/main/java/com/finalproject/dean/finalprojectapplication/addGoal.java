package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

/**
 * Created by Dean on 23/03/2015.
 */
public class addGoal extends Activity{
    EditText goalname;
    String timeSelection;
    LoginDataBaseAdapter db;
    SessionManager session;
    monitor moninstance;
    InputFilter[] goalNameFilter;
    InputFilter[] goalTargetFilter;
    int targetSteps=0;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    MyApplication gs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addgoal);


        // Session class instance
        session = new SessionManager(getApplicationContext());
        moninstance = new monitor();

        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        goalNameFilter= new InputFilter[1];
        goalNameFilter[0] = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedUsernameChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedUsernameChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };

        goalTargetFilter= new InputFilter[1];
        goalTargetFilter[0] = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedUsernameChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedUsernameChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };

        goalname=(EditText)findViewById(R.id.goalname);
        goalname.setFilters(goalNameFilter);
        EditText targetValue = (EditText) findViewById(R.id.targetBox);
        targetValue.setFilters(goalTargetFilter);


    }

    public void startTimer(String timeSelection, Calendar c) {
        gs = (MyApplication) getApplication();
        timer = gs.getTimer();
        initializeTimerTask();

        switch(timeSelection) {
            case "Day": {
                timer.schedule(timerTask, 86400000);
                break;
            }
            case "Week": {
                timer.schedule(timerTask, 604800000);
                break;
            }
            case "Month": {
                timer.schedule(timerTask, 1628000000 + 1628000000);
                break;
            }
        }
    }


    // Methods to handleClick Event of Sign In Button
    public void clickBackBtn(View V)
    {
        Intent intentSettingsScreen=new Intent(getApplicationContext(),monitor.class);
        startActivity(intentSettingsScreen);
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {

                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Failed Goal", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                });

            }
        };
    }



    public void clickStart(View V)
    {
        goalname=(EditText)findViewById(R.id.goalname);

        String goalName=goalname.getText().toString();

        RadioGroup timeGroup = (RadioGroup) findViewById(R.id.timeGroup);
        EditText targetValue = (EditText) findViewById(R.id.targetBox);
        String targetGoal=targetValue.getText().toString();
        if(timeGroup.getCheckedRadioButtonId()!=-1){
            int id= timeGroup.getCheckedRadioButtonId();
            View radioButton = timeGroup.findViewById(id);
            int radioId = timeGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) timeGroup.getChildAt(radioId);
            timeSelection = (String) btn.getText();
        }

        // check if any of the fields are vaccant
        if(goalName.equals("")|| timeSelection.equals("") || targetGoal.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Vacant Field", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            try {
                targetSteps = Integer.parseInt(targetValue.getText().toString());
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            db.insertNewGoal(goalName, timeSelection,targetSteps);
            session.createGoalPref(targetSteps);
            Calendar c = Calendar.getInstance();
            startTimer(timeSelection, c);
            Toast.makeText(getApplicationContext(), "Goal Started ", Toast.LENGTH_LONG).show();

        }



    }

}

