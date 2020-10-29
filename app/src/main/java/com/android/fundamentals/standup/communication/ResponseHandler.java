package com.android.fundamentals.standup.communication;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ResponseHandler implements  Response.Listener<String>, Response.ErrorListener{
    ServerResultHandler listener;
    int recipient;

    public ResponseHandler(ServerResultHandler listener, int recipient) {
        this.listener = listener;
        this.recipient = recipient;
    }

    public void onResponse(String response) {
        JsonElement result = JsonParser.parseString(response);
        listener.onNewResult(result, recipient);
    }

    public void onErrorResponse(VolleyError error) {

        System.out.println(error.toString());
    }

    public interface ServerResultHandler {
        public void onNewResult(JsonElement result, int recipient);
    }
}
