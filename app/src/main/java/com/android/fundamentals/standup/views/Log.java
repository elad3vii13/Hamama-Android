package com.android.fundamentals.standup.views;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        bundle.putInt("recipient", CommService.LOG_RECIPIENT);
        super.onNewSettings(bundle);
    }

    @Override
    public boolean showPriority() {
        return true;
    }

    @Override
    protected void onBroadcastReceived(Intent intent) {
        switch(intent.getAction()){
            case CommService.NEW_LOG_DATA:
                String  mdata = intent.getStringExtra("dataResponse");
                LogFragment logFrag = (LogFragment) fmgr.findFragmentById(R.id.logFrag);
                logFrag.refreshLog(mdata);
                break;
            case CommService.NEW_SENSORS_LIST:
                super.onBroadcastReceived(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void clearDisplay() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        fmgr = getFragmentManager();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_LOG_DATA);
        registerReceiver(drr, intentFilter);
    }


}

