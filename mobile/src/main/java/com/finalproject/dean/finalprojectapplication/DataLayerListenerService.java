package com.finalproject.dean.finalprojectapplication;

/**
 * Created by Dean on 03/04/2015.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

public class DataLayerListenerService extends WearableListenerService {

    private static final String LOG_TAG = "WearableListener";
    public static final String HEARTBEAT = "HEARTBEAT";
    public static final String STEPCOUNT = "STEPCOUNT";

    private static Handler handler;
    private static Handler stephandler;
    private static Handler finishhandler;
    private static int currentValue=0; //Heart & Step
    private static int stepValue=0;
    private static Message finishVal;

    public static Handler getHandler() {
        return handler;
    }
    public static Handler getFinishHandler() {
        return finishhandler;
    }
    public static Handler getStepHandler() {
        return stephandler;
    }

    public static void setHandler(Handler handler) {
        DataLayerListenerService.handler = handler;
        // send current value as initial value.
        if(handler!=null) {
            Log.d(LOG_TAG, "handler heart send" + currentValue);
            handler.sendEmptyMessage(currentValue);
        }
    }

    public static void setFinishHandler(Handler finishhandler) {
        DataLayerListenerService.finishhandler = finishhandler;
        // send value as initial value.
        if(handler!=null) {
            Log.d(LOG_TAG, "Completed to Handler" + finishVal);
            finishhandler.sendMessage(finishVal);
        }
    }

    public static void setStepHandler(Handler stephandler) {
        DataLayerListenerService.stephandler = stephandler;
        // send current value as initial value.
        if(stephandler!=null) {
            Log.d(LOG_TAG, "handler step send" + stepValue);
            stephandler.sendEmptyMessage(stepValue);
        }
    }
    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d(LOG_TAG, "Connected peer name & ID: " + name + "|" + id);
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if (messageEvent.getPath().equals("HEARTSENSE")){
            // save the new heartbeat value
            DataMap heartVal = new DataMap();
            heartVal = DataMap.fromByteArray(messageEvent.getData());
            currentValue = Integer.parseInt(heartVal.getString("HEART MESSAGE"));
            if(handler!=null) {
                // if a handler is registered, send the value as new message
                Log.d(LOG_TAG, "received a heartbeat message from wear: " + currentValue);
                handler.sendEmptyMessage(currentValue);
            }
        }
        else if (messageEvent.getPath().equals("STEPSENSE")){
            // save the new steps value
            DataMap stepVal = new DataMap();
            stepVal = DataMap.fromByteArray(messageEvent.getData());
            stepValue = Integer.parseInt(stepVal.getString("STEP MESSAGE"));
            if (stephandler != null) {
                // if a handler is registered, send the value as new message
                Log.d(LOG_TAG, "received a step message from wear: " + stepValue);
                stephandler.sendEmptyMessage(stepValue);
            }
        }
        else if (messageEvent.getPath().equals("COMPLETED")){
            // save the COMPLETED value
            DataMap completedVal = new DataMap();
            completedVal = DataMap.fromByteArray(messageEvent.getData());
            String val = finishVal.toString();
            val = completedVal.getString("COMPLETE MESSAGE");
            finishVal = Message.obtain();
            finishVal.obj = val;
            if (stephandler != null) {
                // if a handler is registered, send the value as new message
                Log.d(LOG_TAG, "received the completed message from wear: " + finishVal);
                finishhandler.sendMessage(finishVal);
            }
        }
    }




}
