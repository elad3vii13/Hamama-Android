package com.android.fundamentals.standup.views;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Log extends SensorsBasedActivity {
    FragmentManager fmgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }

    @Override
    public void onNewSettings(Bundle bundle) {
        // call to commService
        Intent intent = new Intent(this, CommService.class);
        //get log list from server
        bundle.putInt("recipient", CommService.LOG_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    @Override
    public void clearDisplay() {

    }





    @Override
    protected void onResume() {
        super.onResume();
        fmgr = getFragmentManager();
    }


}

