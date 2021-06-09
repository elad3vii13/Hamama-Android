package com.android.hamama.application.communication;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/*
    A class which use Volley, in order to get the responses from the server.
    the requests are sent from the CommService activity
 */
public class ResponseHandler implements  Response.Listener<String>, Response.ErrorListener{
    ServerResultHandler listener;
    Integer recipient;

    public ResponseHandler(ServerResultHandler listener, Integer recipient) {
        this.listener = listener;
        this.recipient = recipient;
    }

    // this is the async function, which all the responses from the server goes to when it arrives
    public void onResponse(String response) {
        //    JsonElement result = JsonParser.parseString(response);
        listener.onNewResult(response, recipient);
    }

    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }

    /* an interface, which its purpose is to tell the CommService
       which implements him to call the function onNewResult which is declared in the CommService
       for passing the data for the right place.
     */

    public interface ServerResultHandler {
        public void onNewResult(String result, Integer recipient);
    }
}
