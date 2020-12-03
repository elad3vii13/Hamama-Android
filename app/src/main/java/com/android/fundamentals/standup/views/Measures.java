package com.android.fundamentals.standup.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.fundamentals.standup.R;
import com.android.fundamentals.standup.communication.CommService;

public class Measures extends AppCompatActivity implements GraphSettings.GraphSettingsListener {

    FragmentManager fmgr;
    DataResultReceiver drr;

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

    @Override
    public void clearGraph() {
        Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
        graphFrag.clearGraph();
    }

    @Override
    protected void onResume() {
        fmgr = getFragmentManager();
        drr = new DataResultReceiver();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_MEASURE_DATA);
        registerReceiver(drr, intentFilter);
        super.onResume();
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
                case CommService.NEW_MEASURE_DATA:
                    String  mdata = intent.getStringExtra("dataResponse");
                    Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
                    graphFrag.refreshGraph(mdata);
                    break;
                default:
                    break;
            }
        }
    }
}