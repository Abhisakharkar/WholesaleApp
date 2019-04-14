package com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface ResponseReceiveListener {
    public void onResponseReceive(JSONObject responseObject);
    public void onErrorReceive(VolleyError error);
}
