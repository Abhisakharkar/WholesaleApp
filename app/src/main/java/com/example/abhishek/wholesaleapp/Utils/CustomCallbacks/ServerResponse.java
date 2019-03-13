package com.example.abhishek.wholesaleapp.Utils.CustomCallbacks;

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

    public void errorReceived(){
        if (responseReceiveListener != null){
            responseReceiveListener.onErrorReceive();
        }
    }
}
