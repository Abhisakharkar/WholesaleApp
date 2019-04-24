package com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.UniversalCallbacks;

import org.json.JSONObject;

public interface JsonDataCallback {
    public void onSuccess(JSONObject responseData);
    public void onFailure(JSONObject errorData);
}
