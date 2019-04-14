package com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class ServerResponse {

    private ResponseReceiveListener responseReceiveListener;

    public void setResponseReceiveListener(ResponseReceiveListener responseReceiveListener) {
        this.responseReceiveListener = responseReceiveListener;
    }

    public void responseReceived(JSONObject responseObject){
        if (responseReceiveListener != null){
            responseReceiveListener.onResponseReceive(responseObject);
        }
    }

    public void errorReceived(VolleyError error){
        if (responseReceiveListener != null){
            responseReceiveListener.onErrorReceive(error);
        }
    }
}
