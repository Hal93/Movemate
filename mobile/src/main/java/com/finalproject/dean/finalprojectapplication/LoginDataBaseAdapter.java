package com.finalproject.dean.finalprojectapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dean on 13/02/2015.
 */
public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "movemate.db";
    SessionManager session;
    static final int DATABASE_VERSION = 1;

    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+"( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";
    static final String GOAL_TABLE_CREATE = "create table "+"GOALS"+"( " +"goalID"+" integer primary key autoincrement,"+ "GOALNAME  text,GOALTIME text, GOALTARGET integer); ";
    static final String ACTIVITIES_TABLE_CREATE = "create table "+"ACTIVITIES"+"( " +"  activityID"+" integer primary key autoincrement,"+ "ACTIVITYNAME  text,ACTIVITYTYPE text); ";

    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;
    public  LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to openthe Database
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close()
    {
        db.close();
    }

    // method returns an Instance of the Database
    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    // method to insert a record in Table
    public void insertEntry(String userName, String password) {
        SimpleCrypto encrypt = new SimpleCrypto();
        session = new SessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        String key = user.get(SessionManager.SECRET_KEY);
        try {
            String crypto = SimpleCrypto.encrypt(key, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD",password);

        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        Toast.makeText(context, "User Info Saved", Toast.LENGTH_LONG).show();
    }

    // method to delete a Record of UserName
    public int deleteEntry(String UserName)
    {

        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        //Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;

    }

    // method to get the password  of userName
    public String getSingleEntry(String userName)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        return password;
    }

    // Method to Update an Existing Record
    public void  updateEntry(String userName,String password)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }

    // method to get all goals
    public ArrayList<String> getAllUsers()
    {
        ArrayList<String> userList = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT USERNAME FROM LOGIN", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                userList.add(c.getString(0));
                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }

        return userList;
    }

    /*######################################## GOAL TABLE FUNCTIONS ########################################### */

    public void insertNewGoal(String goalName,String goalTime, int target)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("GOALNAME", goalName);
        newValues.put("GOALTIME",goalTime);
        newValues.put("GOALTARGET",target);

        // Insert the row into your table
        db.insert("GOALS", null, newValues);
    }

    // method to delete a Record of UserName
    public int deleteGoalEntry(String goalName)
    {

        String where="GOALNAME=?";
        int numberOFEntriesDeleted= db.delete("GOALS", where, new String[]{goalName}) ;
        //Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;

    }

    // method to get the goal type  of goal name
    public String getSingleGoalEntry(String goalName)
    {
        Cursor cursor=db.query("GOALS", null, " USERNAME=?", new String[]{goalName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        String goalType= cursor.getString(cursor.getColumnIndex("GOALTYPE"));
        return goalType;
    }

    // Method to Update an Existing Record
    public void  updateGoalEntry(String goalName,String goalType)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("GOALNAME", goalName);
        updatedValues.put("GOALTYPE",goalType);

        String where="GOALNAME = ?";
        db.update("GOALS",updatedValues, where, new String[]{goalName});
    }

    // method to get all goals
    public ArrayList<String> getAllGoals()
    {
        ArrayList<String> goalList = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT GOALNAME FROM GOALS", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                goalList.add(c.getString(0));
                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }

        return goalList;
    }


    /*######################################## ACTIVITY TABLE FUNCTIONS ########################################### */

    public void insertNewActivity(String activityName,String activityType)
    {
        ContentValues newValues = new ContentValues();
        // Assign values for each column.
        newValues.put("ACTIVITYNAME", activityName);
        newValues.put("ACTIVITYTYPE",activityType);

        // Insert the row into your table
        db.insert("ACTIVITIES", null, newValues);
    }

    // method to delete a Record of UserName
    public int deleteActivity(String activityName)
    {
        String where="ACTIVITYNAME=?";
        int numberOFEntriesDeleted= db.delete("ACTIVITIES", where, new String[]{activityName}) ;
        //Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    // method to get all goals
    public ArrayList<String> getAllActivities(String type)
    {
        ArrayList<String> activityList = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT ACTIVITYNAME FROM ACTIVITIES WHERE ACTIVITYTYPE = " + "'" + type + "';" ,null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                activityList.add(c.getString(0));
                //Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
        }

        return activityList;
    }



}
