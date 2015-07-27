package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class activityScreen extends Activity {
    Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter db;
    SessionManager session;
    InputFilter[] inputFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_screen);
        session = new SessionManager(getApplicationContext());

        inputFilter= new InputFilter[1];
        inputFilter[0] = new InputFilter(){
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

        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Get The reference Of Buttons
        btnSignIn=(Button)findViewById(R.id.buttonSignIN);
        btnSignUp=(Button)findViewById(R.id.buttonSignUP);

        // Set OnClick Listener on SignUp button
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /// Create Intent for SignUpActivity  abd Start The Activity
                Intent intentSignUP=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intentSignUP);
            }
        });
    }


    // Methods to handleClick Event of Sign In Button
    public void signIn(View V)
    {

        final Dialog dialog = new Dialog(activityScreen.this);

        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");

        // get the References of views
        final  EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
        final  EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

        editTextUserName.setFilters(inputFilter);
        editTextPassword.setFilters(inputFilter);
        Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                // get The User name and Password
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();

                if(userName.trim().length() < 0 || password.trim().length() < 0)
                {
                    Toast.makeText(getApplicationContext(), "Vacant Field", Toast.LENGTH_LONG).show();
                    return;
                }
                if(userName.trim().length() > 30 || password.trim().length() > 30)
                {
                    Toast.makeText(getApplicationContext(), "Vacant Field", Toast.LENGTH_LONG).show();
                    return;
                }
                String storedPassword = db.getSingleEntry(userName);
                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {

                    String secretKey = null;
                    try {
                        secretKey = activityScreen.generateKey();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    session.createLoginSession(userName, password, secretKey);
                    dialog.dismiss();
                    Intent intentMainScreen=new Intent(getApplicationContext(),main.class);
                    startActivity(intentMainScreen);
                    finish();
                }
                else
                {
                    Toast.makeText(activityScreen.this, "Login Failed", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();



    }

    public static String generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        String encodedKey = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
        return encodedKey;
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Close The Database
       db.close();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
