package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.PieChartView;

 /**
 * Created by Dean on 19/02/2015.
 */

public class monitor extends ActionBarActivity  {
     SessionManager session;
     private TextView heartRead;
     private ImageView heartIcon;
     public TextView stepRead;
     public Message msg;
     public Message stepmsg;
     public Message finishmsg;
     Toolbar toolbar;
     private PieChartView chart;
     private PieChartData data;
     MyApplication gs;

     public Handler handler = new Handler() {
         @Override
         public void handleMessage(Message msg) {
             // message from API client! message from wear! The contents is the heartbeat.
             heartRead = (TextView) findViewById(R.id.HeartCount);
             heartIcon = (ImageView) findViewById(R.id.heartIcon);
             if(heartRead!=null ) {
                 heartRead.setText(Integer.toString(msg.what));
             }

             if(msg.what < 70){
                 heartIcon.setImageResource(R.drawable.badheart);
             }
             else{
                 heartIcon.setImageResource(R.drawable.goodheart);
             }
         }
     };

     public Handler completehandler = new Handler() {
         @Override
         public void handleMessage(Message msg) {
             String userFinish = Integer.toString(msg.what);
             Toast.makeText(monitor.this, userFinish, Toast.LENGTH_SHORT).show();
         }
     };

     public Handler stephandler = new Handler() {
         @Override
         public void handleMessage(Message msg) {
             // message from API client! message from wear! The contents is the heartbeat.
             stepRead = (TextView) findViewById(R.id.stepValue);
             if(stepRead!=null ) {
                 stepRead.setText(Integer.toString(msg.what));
                 session.createStepsPref(msg.what);

             }
         }
     };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo3toolbar);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.btnPicture);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.btnVideo);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.btnAll);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        String password= user.get(SessionManager.PASS_WORD);


        chart = (PieChartView) findViewById(R.id.chart);
        msg = handler.obtainMessage();
        stepmsg = stephandler.obtainMessage();
        finishmsg = completehandler.obtainMessage();

        int stepTarget = session.getTargetPref();
        int currentSteps = session.getStepsPref();

        if(stepTarget != 0){
            generateData(stepTarget, currentSteps);
        }


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String itemString = menuItem.toString();
                switch(itemString){
                    case "Help":{
                        Intent i=new Intent(getApplicationContext(),Help.class);
                        startActivity(i);
                        break;
                    }
                    case "Settings":{
                        Intent intentSettingsScreen=new Intent(getApplicationContext(),settings.class);
                        startActivity(intentSettingsScreen);
                        break;
                    }
                }

                return true;
            }
        });

    }
       // int steps = Integer.toString(msg.w
        //generateData(stepRead.getText(Integer.toString(msg.what)),60);


     private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked) {
                 if(buttonView.getText().equals("Activity")){
                     Intent intentMonitorScreen = new Intent(getApplicationContext(), main.class);
                     startActivity(intentMonitorScreen);
                     overridePendingTransition(R.anim.enter_left, R.anim.exit_left_out);
                 }
                 else if(buttonView.getText().equals("Settings")){
                     Intent intentMonitorScreen = new Intent(getApplicationContext(), settings.class);
                     startActivity(intentMonitorScreen);
                     overridePendingTransition(R.anim.enter_left, R.anim.exit_left_out);
                 }
                 else{
                     return;
                 }
                 //Toast.makeText(main.this, buttonView.getText(), Toast.LENGTH_SHORT).show();
             }
         }
     };

     @Override
     protected void onResume() {
         super.onResume();
         // register our handler with the DataLayerService. This ensures we get messages whenever the service receives something.
         DataLayerListenerService.setHandler(handler);
         DataLayerListenerService.setStepHandler(stephandler);
         
     }

     @Override
     protected void onPause() {
         super.onPause();
         // unregister our handler so the service does not need to send its messages anywhere.
         DataLayerListenerService.setHandler(null);
         DataLayerListenerService.setStepHandler(null);
         DataLayerListenerService.setFinishHandler(null);
     }

     // Methods to handleClick Event of Add Goal In Button
     public void clickAddGoalButton(View V)
     {
         Intent intentSettingsScreen=new Intent(getApplicationContext(),addGoal.class);
         startActivity(intentSettingsScreen);
     }

     // Methods to handleClick Event of Add Goal In Button
     public void clickViewGoals(View V)
     {
         Intent intentSettingsScreen=new Intent(getApplicationContext(),listGoal.class);
         startActivity(intentSettingsScreen);
     }

     public void refreshPage(View V)
     {
         Intent intentSettingsScreen=new Intent(getApplicationContext(),monitor.class);
         startActivity(intentSettingsScreen);
     }

     public void generateData(int goalVal, int stepVal) {
         addGoal ad = new addGoal();
         gs = (MyApplication) getApplication();
         Timer timer = gs.getTimer();
         int adjustGoal = goalVal - stepVal;
         if(adjustGoal <= 0){
             Toast.makeText(monitor.this, "Goal Achieved", Toast.LENGTH_SHORT).show();
             timer.cancel();
         }
         else{
             Log.d("INSIDE GENERATE DATA", "goal val is" + goalVal);
             Log.d("INSIDE GENERATE DATA", "step val is" + stepVal);
             ArrayList<SliceValue> values = new ArrayList<SliceValue>();
             SliceValue stepValue = new SliceValue(stepVal, ChartUtils.COLOR_GREEN);
             SliceValue goalValue = new SliceValue(adjustGoal, ChartUtils.COLOR_VIOLET);

             values.add(stepValue);
             values.add(goalValue);
             data = new PieChartData(values);
             Log.d("Data", "is " + data);
             data.setHasLabels(true);
             data.setCenterText1("Goal");
             data.setHasCenterCircle(true);
             data.setSlicesSpacing(5);
             Log.d("Data", "is " + data);
             chart.setPieChartData(data);
         }

     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu items for use in the action bar

         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.menu_monitor_screen, menu);
         return super.onCreateOptionsMenu(menu);
     }




 }




