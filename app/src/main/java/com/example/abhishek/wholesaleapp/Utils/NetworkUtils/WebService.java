package com.example.abhishek.wholesaleapp.Utils.NetworkUtils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class WebService {

    public static WebService instance;
    public static ServerResponse serverResponse;

    private RequestQueue requestQueue;

    private WebService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        serverResponse = new ServerResponse();
    }

    public static WebService getWebServiceInstance(Context context) {
        if (instance == null) {
            instance = new WebService(context);
        }
        return instance;
    }

    public static ServerResponse getCallbackInstance() {
        return serverResponse;
    }

    private void sendRequest(String requestMethod, String path, JSONObject headers, final JSONObject requestBody) {
        String URL = "BaseUrl" + path;
        int method = 0;
        if (requestMethod.equals("POST")) {
            method = Request.Method.POST;
        } else if (requestMethod.equals("GET")) {
            method = Request.Method.GET;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, URL, headers
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverResponse.responseReceived(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverResponse.errorReceived();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

}
