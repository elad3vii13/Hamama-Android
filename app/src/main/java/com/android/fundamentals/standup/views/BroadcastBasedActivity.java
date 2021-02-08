package com.android.fundamentals.standup.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

