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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dean on 15/03/2015.
 */
public class MyCustomFinalAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    public ArrayList<String> finalList;
    private Context context;
    MyApplication application;



    public MyCustomFinalAdapter(ArrayList<String> list, Context context) {
        finalList = new ArrayList<String>();
        this.list = list;
        this.context = context;
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
            view = inflater.inflate(R.layout.listfinallayout, null);

        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.goalItem);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        TextView text = (TextView)view.findViewById((R.id.goalItem));



        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicking item", "in list");
                ListView lv = (ListView) v.findViewById(R.id.ActivityList);
                TextView text = (TextView) v.findViewById((R.id.goalItem));
                String item = text.getText().toString();

                MyApplication gs = new MyApplication();
                int size = gs.getArraySize();
                if(size >= 10){
                    Log.d("Too many", "items ");
                    return;

                }else{
                    gs.addtoList(item);
                    Log.d("New Item", "added " + item);
                }

                /*
                if (!finalList.isEmpty() && finalList.size() <= 9) {
                    finalList.add(item);
                    Log.d("Added to Final List", item);
                    notifyDataSetChanged();
                }
                */
            }

        });



        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
                                list.remove(position); // remove from list
                                notifyDataSetChanged(); // notify data changed
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Item will be deleted, are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

            }
        });


        return view;
    }




}
