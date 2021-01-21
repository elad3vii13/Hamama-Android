package com.android.fundamentals.standup.views;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class SensorsBasedActivity extends AppCompatActivity implements DisplaySettings.SettingsListener  {

    String jsonSensors;
    ArrayList<Sensor> sensors;
    SharedPreferences sh1;
    FragmentManager fmgr;
    DataResultReceiver drr;

    @Override
    protected void onResume() {
        drr = new DataResultReceiver();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_SENSORS_LIST);
        registerReceiver(drr, intentFilter);

        fmgr = getFragmentManager();
        sh1 = getSharedPreferences("Shared", MODE_PRIVATE);
        this.jsonSensors = sh1.getString("Sensors", "");
        if(jsonSensors.isEmpty()) {
            refreshSensorsList();
        }
        else{
            Type listType = new TypeToken<List<Sensor>>() {}.getType();
            sensors = new Gson().fromJson(jsonSensors, listType);
            DisplaySettings fragDisplaySettings = (DisplaySettings) fmgr.findFragmentById(R.id.fragDisplaySettings);
            fragDisplaySettings.initGraphSettings();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(drr);
        super.onPause();
    }

    @Override
    public ArrayList<Sensor> getSensorsList() {
        return sensors;
    }


    @Override
    public void refreshSensorsList(){
        Intent intent = new Intent(this, CommService.class);
        //get sensors list from server
        Bundle bundle = new Bundle();
        bundle.putInt("recipient", CommService.MEASURE_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    @Override
    public void onNewSettings(Bundle bundle) {
        Intent intent = new Intent(this, CommService.class);
        bundle.putInt("recipient", CommService.GRAPH_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }




    protected void onBroadcastReceived(Intent intent) {
        switch(intent.getAction()){
            case CommService.NEW_SENSORS_LIST:
                jsonSensors = intent.getStringExtra("dataResponse");
                Type listType = new TypeToken<List<Sensor>>() {}.getType();
                sensors = new Gson().fromJson(jsonSensors, listType);
                DisplaySettings fragDisplaySettings = (DisplaySettings) fmgr.findFragmentById(R.id.fragDisplaySettings);
                fragDisplaySettings.initGraphSettings();
                break;
            default:
                break;
        }
    }
    public class DataResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    }

}
