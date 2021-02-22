package com.android.hamama.application.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.hamama.application.R;
import com.android.hamama.application.communication.CommService;

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