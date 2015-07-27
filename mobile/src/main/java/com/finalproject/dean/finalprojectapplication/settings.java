package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Dean on 26/02/2015.
 */
public class settings  extends ActionBarActivity  {
    LoginDataBaseAdapter db;
    Toolbar toolbar;
    SessionManager session;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

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


        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        // Getting the button and setting an onClick Listener
        Button btn = (Button) findViewById(R.id.removeAcc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });

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
                    overridePendingTransition(R.anim.enter_right, R.anim.exit_right_out);
                }
                else if(buttonView.getText().equals("Monitor")){
                    Intent intentMonitorScreen = new Intent(getApplicationContext(), monitor.class);
                    startActivity(intentMonitorScreen);
                    overridePendingTransition(R.anim.enter_right, R.anim.exit_right_out);
                }
                else{
                    return;
                }
                //Toast.makeText(main.this, buttonView.getText(), Toast.LENGTH_SHORT).show();
            }
        }
    };

            // Methods to handleClick Event of Sign In Button
            public void clickMonitorButton(View V) {
                Intent intentMonitor = new Intent(getApplicationContext(), monitor.class);
                startActivity(intentMonitor);
            }

            // Methods to handleClick Event of Sign In Button
            public void clickHomeButton(View V) {
                Intent intentHome = new Intent(getApplicationContext(), main.class);
                startActivity(intentHome);
            }

            // Method for deleting user account
            public void deleteAccount() {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                                // get user data from session
                                HashMap<String, String> user = session.getUserDetails();
                                String name = user.get(SessionManager.USER_NAME);
                                String password= user.get(SessionManager.PASS_WORD);
                                db.deleteEntry(name);
                                session.logoutUser();
                                Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_LONG).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Account will be deleted, are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }

            public void clickLogout(View V) {
                session.logoutUser();
                Toast.makeText(settings.this, "Logged Out", Toast.LENGTH_LONG).show();
            }

            public void clickUpdate(View V) {
                Intent intentLogin = new Intent(getApplicationContext(), updateAccount.class);
                startActivity(intentLogin);
            }

            public void clickSet(View V) {
                return;
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu items for use in the action bar

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_monitor_screen, menu);
                return super.onCreateOptionsMenu(menu);
            }


}




