package com.android.fundamentals.standup.communication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.fundamentals.standup.views.MainMenu;
import com.android.fundamentals.standup.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CommService extends Service {
    static RequestQueue queue;
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    final int NOTIFICATION_ID1=1;

    public CommService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (queue== null)
            queue = Volley.newRequestQueue(this);
        initForeground();
        new CommThread().start();
        return super.onStartCommand(intent, flags, startId);
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

    private StringRequest createRequest(String url){
        ResponseHandler rph = new ResponseHandler();
        StringRequest request = new StringRequest(Request.Method.GET, url, rph, rph);
        return request;
    }

    private class CommThread extends Thread{
        @Override
        public void run() {
            queue.add(createRequest("http://10.0.2.2:8080/mobile?cmd=measure&sid=1&from=123&to=12345"));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
