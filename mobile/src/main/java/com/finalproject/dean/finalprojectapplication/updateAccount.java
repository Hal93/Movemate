package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dean on 26/02/2015.
 */
public class updateAccount  extends Activity {
    LoginDataBaseAdapter db;
    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    InputFilter[] updateFormFilter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateaccount);

        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        updateFormFilter= new InputFilter[1];
        updateFormFilter[0] = new InputFilter(){
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

        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);

        editTextUserName.setFilters(updateFormFilter);
        editTextPassword.setFilters(updateFormFilter);
        editTextConfirmPassword.setFilters(updateFormFilter);

        // Getting the button and setting an onClick Listener
        Button btn = (Button) findViewById(R.id.updateBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAccount();
            }
        });
    }

            // Methods to handleClick Event of Sign In Button
            public void clickBackButton(View V) {
                Intent intentMonitorScreen = new Intent(getApplicationContext(), settings.class);
                startActivity(intentMonitorScreen);
            }

            // Method for deleting user account
            public void updateAccount() {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // Yes button clicked

                                editTextUserName=(EditText)findViewById(R.id.editTextUserName);
                                editTextPassword=(EditText)findViewById(R.id.editTextPassword);
                                editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);

                                String username=editTextUserName.getText().toString();
                                String password=editTextPassword.getText().toString();
                                String confirmPassword=editTextConfirmPassword.getText().toString();

                                // check if any of the fields are vaccant
                                if(username.equals("")||password.equals("")||confirmPassword.equals(""))
                                {
                                    Toast.makeText(getApplicationContext(), "Fields Vaccant", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                // check if both password matches
                                if(!password.equals(confirmPassword))
                                {
                                    Toast.makeText(getApplicationContext(), "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else
                                {
                                    Log.d("New Username and Password", " are " + username + password);
                                    db.updateEntry(username, password);
                                    Toast.makeText(getApplicationContext(), "Account Successfully Updated ", Toast.LENGTH_LONG).show();
                                }

                                Intent intentLogin = new Intent(getApplicationContext(), settings.class);
                                startActivity(intentLogin);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Account will be updated, are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }

        }


