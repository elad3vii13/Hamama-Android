package com.android.fundamentals.standup.communication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import com.android.fundamentals.standup.views.MainMenu;
import com.android.fundamentals.standup.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CommService extends Service implements ResponseHandler.ServerResultHandler {
    static RequestQueue queue;
    public static final int GRAPH_RECIPIENT =1;
    public static final int MEASURE_RECIPIENT = 2;
    public static final int LOG_RECIPIENT = 3;
    public static final String NEW_GRAPH_DATA = "com.elad.project.commservice.new_measure_data";
    public static final String NEW_SENSORS_LIST = "com.elad.project.commservice.sensors_list";
    public static final String NEW_LOG_DATA = "com.elad.project.commservice.new_log_data";

    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    final int NOTIFICATION_ID1=1;

    public CommService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initForeground();
        if (queue== null)
            queue = Volley.newRequestQueue(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        int recipient = bundle.getInt("recipient");
        String url = buildUrlFromBundle(bundle);
        new CommThread(url, recipient).start();
        return START_REDELIVER_INTENT;
    }

    private String buildUrlFromBundle(Bundle bundle) {
        int recipient = bundle.getInt("recipient");
        String result = "";

         switch(recipient) {
             case GRAPH_RECIPIENT:
                 long from = bundle.getLong("from");
                 long to = bundle.getLong("to");
                 int sensor = bundle.getInt("sensor");
                 result = "http://10.0.2.2:8080/mobile?cmd=measure&sid=" + sensor + "&from=" + from + "&to=" + to;
                 break;

             case MEASURE_RECIPIENT:
                 result = "http://10.0.2.2:8080/mobile?cmd=sensors";
                 break;
         }

        return result;
    }

    private void initForeground(){
        String CHANNEL_ID = "my_channel_01";
        if (mNotiMgr==null)
            mNotiMgr= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"My main channel", NotificationManager.IMPORTANCE_DEFAULT);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Testing Notification...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent);
        startForeground(NOTIFICATION_ID1, updateNotification(""));
    }

    public Notification updateNotification(String details) {
        mNotifyBuilder.setContentText(details).setOnlyAlertOnce(false);
        Notification noti = mNotifyBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotiMgr.notify(NOTIFICATION_ID1, noti);
        return noti;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private StringRequest createRequest(String url, int recipient){
        ResponseHandler rph = new ResponseHandler(this, recipient);
        StringRequest request = new StringRequest(Request.Method.GET, url, rph, rph);
        return request;
    }

    @Override
    public void onNewResult(String result, int recipient) {
        switch(recipient) {
            case GRAPH_RECIPIENT:
                Intent intent = new Intent();
                intent.setAction(NEW_GRAPH_DATA);
                intent.putExtra("dataResponse", result);
                sendBroadcast(intent);
                break;

            case LOG_RECIPIENT: {
                Intent intent3 = new Intent();
                intent3.setAction(NEW_LOG_DATA);
                intent3.putExtra("dataResponse", result);
                sendBroadcast(intent3);
                break;
            }

            case MEASURE_RECIPIENT:
                SharedPreferences sh1 = getSharedPreferences("Shared", MODE_PRIVATE);
                SharedPreferences.Editor editor = sh1.edit();
                editor.putString("Sensors", result);
                editor.commit();
                Intent intent2 = new Intent();
                intent2.setAction(NEW_SENSORS_LIST);
                intent2.putExtra("dataResponse", result);
                sendBroadcast(intent2);
                break;

        }
    }

    private class CommThread extends Thread{
        String url;
        int recipient;
        public CommThread(String url, int recipient){
            this.url = url;
            this.recipient = recipient;
        }

        @Override
        public void run() {
            queue.add(createRequest(url, recipient));

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
