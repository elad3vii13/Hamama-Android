package com.android.hamama.application.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BroadcastBasedActivity extends AppCompatActivity {

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