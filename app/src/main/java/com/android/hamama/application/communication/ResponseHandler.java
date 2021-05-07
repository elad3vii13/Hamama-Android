package com.android.hamama.application.communication;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ResponseHandler implements  Response.Listener<String>, Response.ErrorListener{
    ServerResultHandler listener;
    Integer recipient;

    public ResponseHandler(ServerResultHandler listener, Integer recipient) {
        this.listener = listener;
        this.recipient = recipient;
    }

    public void onResponse(String response) {
        //    JsonElement result = JsonParser.parseString(response);
        listener.onNewResult(response, recipient);
    }

    public void onErrorResponse(VolleyError error) {
        System.out.println(error.toString());
    }

    public interface ServerResultHandler {
        public void onNewResult(String result, Integer recipient);
    }
}
