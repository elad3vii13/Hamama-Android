package com.android.hamama.application.communication;

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

import androidx.preference.PreferenceManager;

import com.android.hamama.application.views.LoginActivity;
import com.android.hamama.application.views.MainMenu;
import com.android.hamama.application.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

/*
   The CommService class - (CommThread in here):
   This class handles the communication between the server and volley by
   building the right url for the appropriate recipient - and building a queue of requests by
   the right format.

   After the result received from the async function, It is sent to the right
   recipient, by creating actions for every kind of request - and broadcasting it.
   That's how the "acitivty of the recipient" knows which broadcast to listen to.
 */
public class CommService extends Service implements ResponseHandler.ServerResultHandler {

    final static String SIGNED_OUT_URL = "http://10.0.2.2:8080/mobile?cmd=logout";

    // The queue of the requests, that executed by the Volley.
    static RequestQueue queue; // RequestQueue, is a type of volley.

    /*  The recipients. this part is needed because

        * the service needs to know where to send the results to
        it does that by setting the right action for that recipient, because the "recipient activity", they are listening
        to that action (so it necessary to know what action to bind).

        * Also, to identify the request and handle it properly, for example by building the url
     */

    public static final int GRAPH_RECIPIENT =1;
    public static final int MEASURE_RECIPIENT = 2;
    public static final int LOG_RECIPIENT = 3;
    public static final int SIGNIN_RECIPIENT = 4;
    public static final int SIGNOUT_RECIPIENT = 5;
    public static final int CURRENT_USER_RECIPIENT = 6;

    /*  The actions, that the recipient is listening to,
        for example the "login", recepient registered that action and he is listening to that action.
     */

    public static final String NEW_GRAPH_DATA = "com.elad.project.commservice.new_measure_data";
    public static final String NEW_SENSORS_LIST = "com.elad.project.commservice.sensors_list";
    public static final String NEW_LOG_DATA = "com.elad.project.commservice.new_log_data";
    public static final String SIGNIN_RESPONSE = "com.elad.project.commservice.signin_response";
    public static final String SIGNOUT_RESPONSE = "com.elad.project.commservice.signout_response";
    public static final String CURRENT_USER_RESPONSE = "com.elad.project.commservice.current_user_response";

    // for the initForeground function, that the service is requires you to run
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    final int NOTIFICATION_ID1=1;

    public CommService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initForeground();

        // Saves the session id as a cookie
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);

        /* This is the queue of the requests, the Volley execute them one by one.
           you can send infinite requests and Volley will handle them quickly
           because it's not waiting for response from the server, he just execute them without
           waiting for response, because Volley is uses asynchronous HTTP requests.
        */

        if (queue == null)
            queue = Volley.newRequestQueue(this);
    }

    /*
        if the user chose not to stay login, and the service was killed
        the application will logout the server automatically
    */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        System.out.println("onTaskRemoved called");
        super.onTaskRemoved(rootIntent);
        //do something you want
        //stop service
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Boolean stay = prefs.getBoolean("stay_loggedin", false);
        if(!stay){
            queue.add(createRequest(SIGNED_OUT_URL, null));
            // new CommThread(SIGNED_OUT_URL, null).start();
        }
        //this.stopSelf();
    }

    /*
        Every time someone request a request, this code will be execute after being called by "start foreground service"
        the code will extract the bundles data and the recipient, and add them to the queue by the format it requires,
        to fit to that format, we'll use the function createReqeust that uses a function from ResponseHandler.

        From that format, we are telling volley where to send the results, after it got it.
        When the result arrives, it will go to the onNewResult function on the service class.
    */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        int recipient = bundle.getInt("recipient");
        String url = buildUrlFromBundle(bundle);
        queue.add(createRequest(url, recipient));
//        new CommThread(url, recipient).start(); // Corrently Disabled
        return START_REDELIVER_INTENT; // if the service was killed, it will be restarted with the same intent
    }

    /*
        This function, will build url from the information of the bundle
        the function knows to do that because of the use of the different "recipients"
        every different url has another purpose, but they're all gathering information from the server
        by sending HTTP requests.
     */

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

             case LOG_RECIPIENT:
                 String priority = bundle.getString("priority", null);
                 long from1 = bundle.getLong("from");
                 long to1 = bundle.getLong("to");

                 int sensor1 = bundle.getInt("sensor", -1);
                 result = "http://10.0.2.2:8080/mobile?cmd=log&from=" + from1 + "&to=" + to1;
                 if (sensor1!= -1) result += "&sid=" + sensor1;
                 if (priority!= null) result += "&priority=" + priority;
                 //System.out.println(result);
                 String something = result;
                 break;

             case MEASURE_RECIPIENT:
                 result = "http://10.0.2.2:8080/mobile?cmd=sensors";
                 break;

             case SIGNIN_RECIPIENT:
                 String nickname = bundle.getString("nickname");
                 String password = bundle.getString("password");
                 result = "http://10.0.2.2:8080/mobile?cmd=login&nickname=" + nickname + "&password=" + password; // nickname and password from bundle
                 break;

             case SIGNOUT_RECIPIENT:
                 result = SIGNED_OUT_URL;
                 break;

             case CURRENT_USER_RECIPIENT:
                 result = "http://10.0.2.2:8080/mobile?cmd=currentUser";
                 break;
         }
        return result;
    }

    /* This function is required by the service, because of the use of foreground service.
       I chose to work with foreground service, because this is marked as "more important", from background service,
       to stop it, you need to do that manually, and the androidOS does not do that automatically.
    */
    private void initForeground(){
        String CHANNEL_ID = "my_channel_01";
        if (mNotiMgr==null)
            mNotiMgr= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"My main channel", NotificationManager.IMPORTANCE_DEFAULT);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Hamama notifications:")
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

    //  USELESS
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*  this function purpose is to fit the url and the recipient to the format of StringRequest
        because this is the format the queue of requests gets.
        in order to create a StringRequest variable, you are required to pass GET (kind of request), url (of the website)
        and the two last variables are responsible of the place of the "RESULTS" and "ERRORS"
    */
    private StringRequest createRequest(String url, Integer recipient){
        ResponseHandler rph = new ResponseHandler(this, recipient);
        StringRequest request = new StringRequest(Request.Method.GET, url, rph, rph);
        return request;
    }

    /* This function, is reponsible for passing the results from the volley.
       the results from the request arrives to the "OnResponse" function on the "ResponseHandler" class.
       from OnResponse, it goes to  the "onNewResult" function on the service class.

        CONTINUE
       the function passing the results, by building an intent which contain the relevant data, set an action()
     */

    @Override
    public void onNewResult(String result, Integer recipient) {
        if (recipient == null) return;

        switch(recipient) {
            case GRAPH_RECIPIENT:
                Intent intent = new Intent();
                intent.setAction(NEW_GRAPH_DATA);
                intent.putExtra("dataResponse", result);
                sendBroadcast(intent);
             //   updateNotification("new Graph data just arrived from the server");
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

            case SIGNIN_RECIPIENT:
                Intent intent4 = new Intent();
                intent4.setAction(SIGNIN_RESPONSE);
                intent4.putExtra("signin_result", result);
                sendBroadcast(intent4);
                break;

            case SIGNOUT_RECIPIENT:
                Intent intent5 = new Intent();
                intent5.setAction(SIGNOUT_RESPONSE);
                sendBroadcast(intent5);
                break;

            case CURRENT_USER_RECIPIENT:
                Intent intent6 = new Intent();
                intent6.setAction(CURRENT_USER_RESPONSE);
                intent6.putExtra("currentUserId", result);
                sendBroadcast(intent6);
                break;
        }
    }

    // I decided to send the request without thread, directly from onStartCommand
    private class CommThread extends Thread {
        String url;
        Integer recipient;

        public CommThread(String url, Integer recipient){
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
