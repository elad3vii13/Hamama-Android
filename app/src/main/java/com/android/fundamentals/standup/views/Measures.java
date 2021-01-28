package com.android.fundamentals.standup.views;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Measures extends SensorsBasedActivity {

    FragmentManager fmgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);
        fmgr = getFragmentManager();
    }

    @Override
    public void onNewSettings(Bundle bundle) {
        bundle.putInt("recipient", CommService.GRAPH_RECIPIENT);
        super.onNewSettings(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_GRAPH_DATA);
        registerReceiver(drr, intentFilter);
    }

    @Override
    public void clearDisplay() {
        Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
        graphFrag.clearGraph();
    }

    @Override
    public boolean showPriority() {
        return false;
    }

    @Override
    protected void onBroadcastReceived(Intent intent) {
        switch(intent.getAction()){
            case CommService.NEW_GRAPH_DATA:
                String  mdata = intent.getStringExtra("dataResponse");
                Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
                graphFrag.refreshGraph(mdata);
                break;
            case CommService.NEW_SENSORS_LIST:
                super.onBroadcastReceived(intent);
                break;
            default:
                break;
        }
    }
}