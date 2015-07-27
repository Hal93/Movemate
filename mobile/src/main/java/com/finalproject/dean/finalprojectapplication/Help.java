package com.finalproject.dean.finalprojectapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Dean on 26/02/2015.
 */
public class Help extends ActionBarActivity  {
    LoginDataBaseAdapter db;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo3toolbar);


        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.btnPicture);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.btnVideo);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);
        radioButton = (RadioButton) findViewById(R.id.btnAll);
        radioButton.setOnCheckedChangeListener(btnNavBarOnCheckedChangeListener);

        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String itemString = menuItem.toString();
                switch(itemString){
                    case "Help":{
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
                if(buttonView.getText().equals("Activity")){
                    Intent intentMonitorScreen = new Intent(getApplicationContext(), main.class);
                    startActivity(intentMonitorScreen);
                    overridePendingTransition(R.anim.enter_right, R.anim.exit_left_out);
                }
                else if(buttonView.getText().equals("Monitor")){
                    Intent intentMonitorScreen = new Intent(getApplicationContext(), monitor.class);
                    startActivity(intentMonitorScreen);
                    overridePendingTransition(R.anim.enter_left, R.anim.exit_right_out);
                }
                else{
                    Intent intentSettingsScreen=new Intent(getApplicationContext(),settings.class);
                    startActivity(intentSettingsScreen);
                    overridePendingTransition(R.anim.enter_left, R.anim.exit_right_out);
                    return;
                }
                //Toast.makeText(main.this, buttonView.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    };


            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu items for use in the action bar

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_monitor_screen, menu);
                return super.onCreateOptionsMenu(menu);
            }


}




