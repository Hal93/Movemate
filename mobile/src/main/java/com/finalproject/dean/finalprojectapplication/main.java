package com.finalproject.dean.finalprojectapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.ListView;

import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.finalproject.dean.finalprojectapplication.Constants;


/**
 * Created by Dean on 14/02/2015.
 */
public class main extends ActionBarActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    SessionManager session;
    private GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    ListView aerobicView;
    ListView balanceView;
    ListView strengthView;
    ListView flexibleView;
    ListView finalView;
    LoginDataBaseAdapter db;
    MyCustomFinalAdapter finaladapter;
    private String data="Send Activity Text";
    ArrayList<String> aerobicList;
    ArrayList<String> balanceList;
    ArrayList<String> strengthList;
    ArrayList<String> finalList;
    private static final String START_ACTIVITY = "/start_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    private static final String COUNT_KEY = "com.example.key.count";
    MyApplication gs;
    private int count = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo3toolbar);

        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.tabActive);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.monitorTab);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.tabSettings);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();


        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        aerobicList = new ArrayList<String>();
        strengthList = new ArrayList<String>();
        balanceList = new ArrayList<String>();
        finalList = new ArrayList<String>();
        gs = (MyApplication) getApplication();
        finalList = gs.getList();


        preloadLists();

        aerobicList = db.getAllActivities("Aerobic");
        strengthList = db.getAllActivities("Strength");
        balanceList = db.getAllActivities("Balance");

        //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(aerobicList, this);
        MyCustomAdapter strengthAdapter = new MyCustomAdapter(strengthList, this);
        MyCustomAdapter balanceAdapter = new MyCustomAdapter( balanceList, this);
        finaladapter = new MyCustomFinalAdapter(finalList, this);

        //handle listview and assign adapter
        aerobicView = (ListView) findViewById(R.id.aerobicList);
        balanceView = (ListView) findViewById(R.id.balanceList);
        strengthView = (ListView) findViewById(R.id.strengthList);
        finalView = (ListView) findViewById(R.id.ActivityList);

        aerobicView.setAdapter(adapter);
        balanceView.setAdapter(balanceAdapter);
        strengthView.setAdapter(strengthAdapter);
        finalView.setAdapter(finaladapter);


        View footerView = ((LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listviewfooter, null, false);
        aerobicView.addFooterView(footerView);
        balanceView.addFooterView(footerView);
        strengthView.addFooterView(footerView);

            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String itemString = menuItem.toString();
                switch(itemString){
                    case "Aerobic":{
                        clickedAerobic();
                        break;
                    }
                    case "Balance":{
                        clickedBalance();
                        break;
                    }
                    case "Strength":{
                        clickedStrength();
                        break;
                    }
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

    private CompoundButton.OnCheckedChangeListener btnNavBarOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if(buttonView.getText().equals("Monitor")){
                    Intent intentMonitorScreen = new Intent(getApplicationContext(), monitor.class);
                    startActivity(intentMonitorScreen);
                    overridePendingTransition(R.anim.enter_left, R.anim.exit_right_out);
                }
                else if(buttonView.getText().equals("Settings")){
                    Intent intentMonitorScreen = new Intent(getApplicationContext(), settings.class);
                    startActivity(intentMonitorScreen);
                    overridePendingTransition(R.anim.enter_left, R.anim.exit_right_out);
                }
                else{
                    return;
                }
                //Toast.makeText(main.this, buttonView.getText(), Toast.LENGTH_SHORT).show();
            }
        /*
            for (String s: finalList) {
                //Do your stuff here
                Log.d("Here are the", "list items " + s);
            }
        */
        }
    };

    public void preloadLists(){
        ArrayList<String>tempAerobic = new ArrayList<String>();
        ArrayList<String>tempStrength = new ArrayList<String>();
        ArrayList<String>tempBalance = new ArrayList<String>();
        tempAerobic = db.getAllActivities("Aerobic");
        tempStrength = db.getAllActivities("Aerobic");
        tempBalance = db.getAllActivities("Aerobic");
        if(tempAerobic.isEmpty() && tempStrength.isEmpty() && tempBalance.isEmpty()){
            Log.d("PRELOAD LISTS","");
            db.insertNewActivity("Let's Go Skate", "Aerobic");
            db.insertNewActivity("Rollerblade", "Aerobic");
            db.insertNewActivity("Rowing", "Aerobic");
            db.insertNewActivity("Time To Dance", "Aerobic");
            db.insertNewActivity("Play Tag", "Aerobic");
            db.insertNewActivity("10 Jump Jacks", "Aerobic");
            db.insertNewActivity("Lets Bike", "Aerobic");
            db.insertNewActivity("Skipping", "Aerobic");
            db.insertNewActivity("Score 10 Football Goals", "Aerobic");
            db.insertNewActivity("Swim", "Aerobic");
            db.insertNewActivity("Rugby Game", "Aerobic");
            db.insertNewActivity("Basketball", "Aerobic");
            db.insertNewActivity("Cartwheel", "Aerobic");
            db.insertNewActivity("Do One Chore", "Aerobic");
            db.insertNewActivity("Clean Your Bedroom", "Aerobic");
            db.insertNewActivity("Let's Race, RUN!", "Aerobic");
            db.insertNewActivity("Time to Walk", "Aerobic");

            db.insertNewActivity("Tug of War", "Strength");
            db.insertNewActivity("Climb A Tree", "Strength");
            db.insertNewActivity("Help Dad Carry Tools", "Strength");
            db.insertNewActivity("Carry A Tray", "Strength");
            db.insertNewActivity("Lets go Rowing", "Strength");
            db.insertNewActivity("Lets Run!", "Strength");
            db.insertNewActivity("Drink Glass Of Milk", "Strength");
            db.insertNewActivity("5 Push Ups", "Strength");
            db.insertNewActivity("10 Push Ups", "Strength");
            db.insertNewActivity("Beat Mum in Sit Ups", "Strength");
            db.insertNewActivity("Plank", "Strength");
            db.insertNewActivity("Do the Crab", "Strength");
            db.insertNewActivity("Monkey Bars", "Strength");
            db.insertNewActivity("Eat Some Green Veggies", "Strength");

            db.insertNewActivity("Do A Handstand", "Balance");
            db.insertNewActivity("Cartwheel Time", "Balance");
            db.insertNewActivity("Balance on a cushion", "Balance");
            db.insertNewActivity("Tightrope Challenge", "Balance");
            db.insertNewActivity("Hop Scotch", "Balance");
            db.insertNewActivity("Freeze", "Balance");
            db.insertNewActivity("Game of Twister", "Balance");
            db.insertNewActivity("Shake On One Leg", "Balance");
            db.insertNewActivity("Dont Drop Knee Ball", "Balance");
            db.insertNewActivity("Exercise Ball Balance", "Balance");
            db.insertNewActivity("Trampoline Stand", "Balance");
            db.insertNewActivity("Stand in Bouncy Castle", "Balance");
            db.insertNewActivity("Stand on Football", "Balance");
        }
        else{
            return;
        }
    }



    // Methods to handleClick Event of Aerobic Button
    public void clickedAerobic() {
        aerobicView = (ListView) findViewById(R.id.aerobicList);
        balanceView = (ListView) findViewById(R.id.balanceList);
        flexibleView = (ListView) findViewById(R.id.flexibleList);
        strengthView = (ListView) findViewById(R.id.strengthList);
        if (aerobicView.getVisibility() == View.VISIBLE) {
            aerobicView.setVisibility(View.GONE);
            finaladapter.notifyDataSetChanged();

        } else {
            aerobicView.setVisibility(View.VISIBLE);
            balanceView.setVisibility(View.GONE);
            flexibleView.setVisibility(View.GONE);
            strengthView.setVisibility(View.GONE);
        }
    }

    // Methods to handleClick Event of Aerobic Button
    public void clickedBalance() {
        aerobicView = (ListView) findViewById(R.id.aerobicList);
        balanceView = (ListView) findViewById(R.id.balanceList);
        flexibleView = (ListView) findViewById(R.id.flexibleList);
        strengthView = (ListView) findViewById(R.id.strengthList);
        if ( balanceView.getVisibility() == View.VISIBLE) {
            balanceView.setVisibility(View.GONE);
            finaladapter.notifyDataSetChanged();


        } else {
            balanceView.setVisibility(View.VISIBLE);
            aerobicView.setVisibility(View.GONE);
            flexibleView.setVisibility(View.GONE);
            strengthView.setVisibility(View.GONE);
        }
    }

    // Methods to handleClick Event of Aerobic Button
    public void clickedStrength() {
        aerobicView = (ListView) findViewById(R.id.aerobicList);
        balanceView = (ListView) findViewById(R.id.balanceList);
        flexibleView = (ListView) findViewById(R.id.flexibleList);
        strengthView = (ListView) findViewById(R.id.strengthList);
        if ( strengthView.getVisibility() == View.VISIBLE) {
            strengthView.setVisibility(View.GONE);
            finaladapter.notifyDataSetChanged();

        } else {
            strengthView.setVisibility(View.VISIBLE);
            aerobicView.setVisibility(View.GONE);
            flexibleView.setVisibility(View.GONE);
            balanceView.setVisibility(View.GONE);
        }
    }

    public void clickSend(View V) {
        Log.d("SEND MESSAGE ON MOBILE", "to Listener");

        if (mGoogleApiClient.isConnected()) {
            Log.d("IN SEND ON MOBILE", "HERE!");
            finalList = gs.getList();
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");
            putDataMapReq.getDataMap().putString("ID", START_ACTIVITY);
            putDataMapReq.getDataMap().putInt(COUNT_KEY, count++);
            putDataMapReq.getDataMap().putStringArrayList("DATA_STRING", finalList);
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
            Log.d("IN SEND ON MOBILE", "MESSAGE SENT!");
        }

    }

    public void addActivity(View V){
        Intent intentMonitorScreen = new Intent(getApplicationContext(), addActivity.class);
        startActivity(intentMonitorScreen);
    }






    @Override
    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
