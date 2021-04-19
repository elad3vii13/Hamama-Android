package com.android.hamama.application.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.hamama.application.R;
import com.android.hamama.application.communication.CommService;
import com.android.hamama.application.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class SensorsBasedActivity extends SignedInBasedActivity implements DisplaySettings.SettingsListener  {
    String jsonSensors;
    ArrayList<Sensor> sensors;
    SharedPreferences sh1;
    FragmentManager fmgr;
    @Override
    protected void onResume() {
        super.onResume(); // Must be the first line
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
}
