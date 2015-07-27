package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dean on 13/02/2015.
 */
public class SignUpActivity extends Activity
{

    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    Button btnCreateAccount;
    LoginDataBaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter(){
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


        // Get References of Views
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
        editTextUserName.setFilters(filters);
        editTextPassword.setFilters(filters);
        btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                String confirmPassword=editTextConfirmPassword.getText().toString();

                ArrayList<String> userList = new ArrayList<String>();
                userList = db.getAllUsers();
                if(userList.contains(userName)){
                    Toast.makeText(getApplicationContext(), "Username taken, Please try another", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(userName.length() > 30 || password.length() > 30)
                {
                    Toast.makeText(getApplicationContext(), "Information Too Long", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if any of the fields are vaccant
                else if(userName.length() < 0 || password.length() < 0 || confirmPassword.length() < 0)
                {
                    Toast.makeText(getApplicationContext(), "Vacant Field", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                else if(!password.equals(confirmPassword))
                {
                    Toast.makeText(getApplicationContext(), "Check Password Fields", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    db.insertEntry(userName, password);
                    Log.d("New User", " created with name " + userName + password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void clickBackButton(View V)
    {
        Intent intentSettingsScreen=new Intent(getApplicationContext(),activityScreen.class);
        startActivity(intentSettingsScreen);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}