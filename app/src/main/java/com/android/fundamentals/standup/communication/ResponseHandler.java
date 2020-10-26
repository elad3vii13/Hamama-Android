package com.android.fundamentals.standup.communication;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

public class ResponseHandler implements  Response.Listener<String>, Response.ErrorListener{

    GraphResultHandler listener;

    public ResponseHandler() {
        //this.listener = listener;
    }

    public void onResponse(String response) {
        JSONArray result = null;
        try {
            result = new JSONArray(response);
            Log.i("ResponseHandler", Integer.toString(result.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onErrorResponse(VolleyError error) {

        System.out.println(error.toString());
    }

    public interface GraphResultHandler {
        public void onNewResult();
    }
}
