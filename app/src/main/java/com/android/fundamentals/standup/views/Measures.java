package com.android.fundamentals.standup.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;

public class Measures extends AppCompatActivity implements GraphSettings.GraphSettingsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);
    }

    @Override
    public void onNewGraphSettings(Bundle bundle) {
        Intent intent = new Intent(this, CommService.class);
        bundle.putInt("recipient", CommService.MEASURE_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }
}