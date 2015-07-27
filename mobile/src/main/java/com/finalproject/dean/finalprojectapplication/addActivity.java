package com.finalproject.dean.finalprojectapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Dean on 20/04/2015.
 */
public class addActivity extends Activity {
    EditText activityField;
    LoginDataBaseAdapter db;
    String typeSelection;
    InputFilter[] inputFilter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addactivity);

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

        activityField=(EditText)findViewById(R.id.activename);
        activityField.setFilters(inputFilter);
        // create the instance of Database
        db=new LoginDataBaseAdapter(this);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void itemClicked(View V) {

        Toast.makeText(addActivity.this, "Clicked item 1", Toast.LENGTH_SHORT).show();
    }

    // Methods to handleClick Event of Sign In Button
    public void clickBackBtn(View V)
    {
        Intent intentSettingsScreen=new Intent(getApplicationContext(), main.class);
        startActivity(intentSettingsScreen);
    }


    public void clickAdd(View V)
    {
        activityField=(EditText)findViewById(R.id.activename);
        String activityName =activityField.getText().toString();

        RadioGroup type = (RadioGroup) findViewById(R.id.typeGroup);
        if(type.getCheckedRadioButtonId()!=-1){
            int id= type.getCheckedRadioButtonId();
            View radioButton = type.findViewById(id);
            int radioId = type.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) type.getChildAt(radioId);
            typeSelection = (String) btn.getText();
        }

        // check if any of the fields are vaccant
        if(activityName.equals("")) {
            Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            db.insertNewActivity(activityName, typeSelection);
            Toast.makeText(getApplicationContext(), "Activity Added", Toast.LENGTH_LONG).show();
        }



    }

}



