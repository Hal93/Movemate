package com.finalproject.dean.finalprojectapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dean on 15/03/2015.
 */
public class MyCustomGoalAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    LoginDataBaseAdapter db;
    Goals goal;

    public MyCustomGoalAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        Goals goal = new Goals();
        db=new LoginDataBaseAdapter(context);
        try {
            db=db.open();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listgoallayout, null);

        }

        //Handle TextView and display string from your list
        TextView goalNameText = (TextView)view.findViewById(R.id.goalItem);
        TextView listTimeText = (TextView)view.findViewById(R.id.time);
        goalNameText.setText(list.get(position));
        //listTimeText.setText(goal);

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);


        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                                String text = list.get(position);
                                Log.d("The text of this item is ", text);
                                db.deleteGoalEntry(text);
                                list.remove(position); // remove from list
                                notifyDataSetChanged(); // notify data changed
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Goal will be deleted, are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

            }
        });

        return view;
    }



}
