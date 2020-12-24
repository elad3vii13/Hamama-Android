package com.android.fundamentals.standup.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;
import com.android.fundamentals.standup.model.Measure;
import com.android.fundamentals.standup.model.Sensor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Measures extends AppCompatActivity implements GraphSettings.GraphSettingsListener {


    SharedPreferences sh1;
    FragmentManager fmgr;
    DataResultReceiver drr;
    String jsonSensors;
    ArrayList<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);

         sh1 = getSharedPreferences("Shared", MODE_PRIVATE);
    }

    @Override
    public void onNewGraphSettings(Bundle bundle) {
        Intent intent = new Intent(this, CommService.class);
        bundle.putInt("recipient", CommService.GRAPH_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    @Override
    public void clearGraph() {
        Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
        graphFrag.clearGraph();
    }

    @Override
    public ArrayList<Sensor> getSensorsList() {
        return sensors;
    }

    @Override
    public void refreshSensorListFromServer() {
        refreshSensorsList();
    }

    @Override
    protected void onResume() {
        fmgr = getFragmentManager();
        this.jsonSensors = sh1.getString("Sensors", "");
        if(jsonSensors.isEmpty()) {
            refreshSensorsList();
        }
        else{
            Type listType = new TypeToken<List<Sensor>>() {}.getType();
            sensors = new Gson().fromJson(jsonSensors, listType);
            GraphSettings fragGraphSettings = (GraphSettings) fmgr.findFragmentById(R.id.fragGraphSettings);
            fragGraphSettings.initGraphSettings();
        }


        drr = new DataResultReceiver();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_GRAPH_DATA);
        intentFilter.addAction(CommService.NEW_SENSORS_LIST);
        registerReceiver(drr, intentFilter);
        super.onResume();
    }

    public void refreshSensorsList(){
        Intent intent = new Intent(this, CommService.class);
        //get sensors list from server
        Bundle bundle = new Bundle();
        bundle.putInt("recipient", CommService.MEASURE_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(drr);
        super.onPause();
    }

    public class DataResultReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case CommService.NEW_GRAPH_DATA:
                    String  mdata = intent.getStringExtra("dataResponse");
                    Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
                    graphFrag.refreshGraph(mdata);
                    break;
                case CommService.NEW_SENSORS_LIST:
                    jsonSensors = intent.getStringExtra("dataResponse");
                    Type listType = new TypeToken<List<Sensor>>() {}.getType();
                    sensors = new Gson().fromJson(jsonSensors, listType);
                    GraphSettings fragGraphSettings = (GraphSettings) fmgr.findFragmentById(R.id.fragGraphSettings);
                    fragGraphSettings.initGraphSettings();
                    break;
                default:
                    break;
            }
        }
    }
}