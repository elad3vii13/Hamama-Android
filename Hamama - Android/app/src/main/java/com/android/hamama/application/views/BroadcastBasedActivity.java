package com.android.hamama.application.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BroadcastBasedActivity extends AppCompatActivity {

    /*
        The purpose of this class is to enable the use of BroadcastReceiver
        any class which extent from this class, will create a 'drr'
        which is an object that extends from BroadcastReceiver.

        When the broadcast arrives it will arrive to the onReceive
        and then to the function 'onBroadcastReceived'
        which declared in the class that extends from it.
    */

    protected abstract void onBroadcastReceived(Intent intent);
    DataResultReceiver drr;

    @Override
    protected void onPause() {
        unregisterReceiver(drr);
        super.onPause();
    }

    @Override
    protected void onResume() {
        drr = new DataResultReceiver();
        super.onResume();
    }

    public class DataResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    }
}