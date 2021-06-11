package com.android.hamama.application.views;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.hamama.application.R;
import com.android.hamama.application.communication.CommService;

/*
    This is the activity of the logs, in this activity the fragments [displaySettings, LogFragment]
    are located.

    extends from SensorBasedActivity, and implements some of the functions from the 'DisplaySettings.SettingsListener'
    interface.
*/

public class Log extends SensorsBasedActivity {
    FragmentManager fmgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
    }

    /*
        declares the function of the interface of displaySettings,
        I didn't do that on SensorBasedActivity (who implements the interface)
        because sensorBasedActivity, contain all the shared functions to the 'Measures' and 'Log'

        And I want to add different recipients to different requests.
    */

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

//            case CommService.NEW_SENSORS_LIST:
//                super.onBroadcastReceived(intent);
//                break;

            default:
                super.onBroadcastReceived(intent);
                break;
        }
    }

    @Override
    public void clearDisplay() {
    }

    /*
        if I get a broadCast response of new data of the log, I would like to
        pass that on the function on the graph, that takes care of the organization of the data
        with given 'json' array.
     */
    @Override
    protected void onResume() {
        super.onResume();
        fmgr = getFragmentManager();
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_LOG_DATA);
        registerReceiver(drr, intentFilter);
    }


}

