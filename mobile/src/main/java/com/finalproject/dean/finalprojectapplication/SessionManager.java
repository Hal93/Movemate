package com.finalproject.dean.finalprojectapplication;

/**
 * Created by Dean on 18/04/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Timer;

import javax.crypto.SecretKey;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "loginprefs";

    // Sharedpref file name
    public static final String TARGET_STEPS = "targetpref";
    public static final String CURRENT_STEPS = "steppref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String USER_NAME = "name";
    public static final String SECRET_KEY = "secret";

    // password (make variable public to access from outside)
    public static final String PASS_WORD = "password";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String password, String secretKey){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(USER_NAME, name);

        // Storing email in pref
        editor.putString(PASS_WORD, password);

        editor.putString(SECRET_KEY, secretKey);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, activityScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(USER_NAME, pref.getString(USER_NAME, null));

        // user password
        user.put(PASS_WORD, pref.getString(PASS_WORD, null));

        // return user
        return user;
    }

    public int getTargetPref(){
        int target=0;

        // user password
        target = pref.getInt(TARGET_STEPS, 0);

        // return user
        return target;
    }

    public int getStepsPref(){
        int steps=0;

        // user password
        steps = pref.getInt(CURRENT_STEPS, 0);

        // return user
        return steps;
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, activityScreen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createGoalPref(int target){

        // Storing name in pref
        editor.putInt(TARGET_STEPS, target);

        // commit changes
        editor.commit();
    }

    public void createStepsPref(int steps){

        // Storing name in pref
        editor.putInt(CURRENT_STEPS, steps);

        // commit changes
        editor.commit();
    }

    public void storeTimerPref(String timer){

        // Storing name in pref
        editor.putString("TIMER", timer);

        // commit changes
        editor.commit();
    }
}
