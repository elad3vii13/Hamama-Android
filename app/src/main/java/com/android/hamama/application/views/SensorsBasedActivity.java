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

    /*
        The purpose of this class is to declare the functions of the interface on DisplaySettings.SettingsListener.
        most of the functions of this interface are the same for the both of the 'Measures' and 'Log' activities
        (which contains the DisplaySettings and the Graph/LogFragment fragments).

        The showPriority and clearDisplay are different from each other, so they are defined differently
        that is the reason they are not defined here.

        This class extends from 'SignedInBasedActivity' because all of the screens which extends this class
        uses the menu, and uses the BroadcastReceiver.
    */

    String jsonSensors;
    ArrayList<Sensor> sensors;
    SharedPreferences sh1;
    FragmentManager fmgr;

    @Override
    protected void onResume() {
        super.onResume(); // Must be the first line, because the broadcastReceiver 'drr' defined on the upper level of inheritance.
        IntentFilter intentFilter = new IntentFilter(CommService.NEW_SENSORS_LIST);
        registerReceiver(drr, intentFilter);

        fmgr = getFragmentManager();

        /*
            Instead of grabbing the list of the sensor every time the 'measures' / 'log' screen
            finished to load from the server,
            I'll save that on the sharedPreferences and everytime I open one of those screens
            it will check the sharedPreferences and search for that list of sensors
            and request will be sent only of a case which the list is empty.
        */

        sh1 = getSharedPreferences("Shared", MODE_PRIVATE);
        this.jsonSensors = sh1.getString("Sensors", "");
        if(jsonSensors.isEmpty()) {
            refreshSensorsList();
        }
        else {
            /* example of jsonSensors value = [{"id":1,"displayName":"מוליכות","name":"EC","units":"S/m"},
                                               {"id":2,"displayName":"חומציות","name":"PH","units":" "}
               After those lines of code, sensors will fit to a format of arrayList<Sensor> from the data from 'sensors' */
            Type listType = new TypeToken<List<Sensor>>() {}.getType();
            sensors = new Gson().fromJson(jsonSensors, listType);

            // in order to get access to DisplaySettings functions, to reset the display settings
            DisplaySettings fragDisplaySettings = (DisplaySettings) fmgr.findFragmentById(R.id.fragDisplaySettings);
            fragDisplaySettings.initGraphSettings();
        }
    }

    // returns the 'sensors' in the ArrayList<Sensor> type (not json)
    @Override
    public ArrayList<Sensor> getSensorsList() {
        return sensors;
    }

    // Send a request for the sensors list from the server
    @Override
    public void refreshSensorsList(){
        Intent intent = new Intent(this, CommService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("recipient", CommService.MEASURE_RECIPIENT);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    /* Sends a request to the server, with a given bundle
       in the 'measures' and 'log' activities, there's an addition
       of the 'recipient' to the bundle, because It's have to be defined - with
       that information the right url is being build, and the result will
       return to the right place.
    */

    @Override
    public void onNewSettings(Bundle bundle) {
        Intent intent = new Intent(this, CommService.class);
        intent.putExtras(bundle);
        startForegroundService(intent);
    }

    /*
        because sensorBasedActivity, extends from 'SignedBasedActivity' which extends from broadcastReceiver
        the class receives all the broadcasts.
         to know which broadcast to listen to, I created an action for
        every kind of broadcast, so the activity would know what action to respond to.
        in this example, the activity will wait until broadcast that associate with 'NEW_SENSORS_LIST' will arrive.
    */

    protected void onBroadcastReceived(Intent intent) {
        switch(intent.getAction()){
            case CommService.NEW_SENSORS_LIST:
                jsonSensors = intent.getStringExtra("dataResponse");
                Type listType = new TypeToken<List<Sensor>>() {}.getType();
                sensors = new Gson().fromJson(jsonSensors, listType);
                DisplaySettings fragDisplaySettings = (DisplaySettings) fmgr.findFragmentById(R.id.fragDisplaySettings);
                /*
                    we don't need to pass the 'sensors' variable because the function initGraphSettings will call
                    the function 'getSensorList()' later.
                 */

                fragDisplaySettings.initGraphSettings();
                break;
            default:
                break;
        }
    }
}