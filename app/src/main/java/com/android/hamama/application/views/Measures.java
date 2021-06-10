package com.android.hamama.application.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.hamama.application.R;
import com.android.hamama.application.communication.CommService;

public class Measures extends SensorsBasedActivity {
    FragmentManager fmgr;

    /*
        This is the activity of measures, in this activity the fragments [displaySettings, Graph]
        are located.

        extends from SensorBasedActivity, and implements some of the functions from the 'DisplaySettings.SettingsListener'
        interface.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measures);
        fmgr = getFragmentManager();
    }


    /*
        declares the function of the interface of displaySettings,
        I didn't do that on SensorBasedActivity (who implements the interface)
        because sensorBasedActivity, contain all the shared functions to the 'Measures' and 'Log'

        And I want to add different recipients to different requests.
    */

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

    /*
        declares the function of the interface of displaySettings,
        I didn't do that on SensorBasedActivity (who implements the interface)
        because sensorBasedActivity, contain all the shared functions to the 'Measures' and 'Log'
    */

    @Override
    public void clearDisplay() {
        Graph graphFrag = (Graph) fmgr.findFragmentById(R.id.fragGraph);
        graphFrag.clearGraph();
    }

    /*
        Function, that tells me if this is the log activity, or the graph
        because the log activity returns true for that function.
     */

    @Override
    public boolean showPriority() {
        return false;
    }

    /*
        if I get a broadCast response of new data of the graph, I would like to
        pass that on the function on the graph, that takes care of the organization of the data
        with given 'json' array.

        Also, if I get a response of sensor list, I would like to refresh the DisplaySettings.
        I do that here, because I don't have a list of sensors on the log activity.
        ????????????????????????
    */

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