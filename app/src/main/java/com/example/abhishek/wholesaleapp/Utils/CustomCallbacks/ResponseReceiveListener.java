package com.example.abhishek.wholesaleapp.Utils.CustomCallbacks;

import org.json.JSONObject;

public interface ResponseReceiveListener {
    public void onResponseReceive(JSONObject responseObject);
    public void onErrorReceive();
}
