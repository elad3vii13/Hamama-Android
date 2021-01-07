package com.android.fundamentals.standup.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Sensor;

import java.util.ArrayList;

public class Log extends AppCompatActivity implements GraphSettings.SettingsListener {

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
    public ArrayList<Sensor> getSensorsList() {
        return null;
    }

    @Override
    public void refreshSensorListFromServer() {

    }
}