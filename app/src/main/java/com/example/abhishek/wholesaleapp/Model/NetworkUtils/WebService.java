package com.example.abhishek.wholesaleapp.Model.NetworkUtils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class WebService {

    private String TAG = "WebService";

    public static WebService instance;
    public static ServerResponse serverResponse;
    private Context context;
    public Certificate ca;

    private RequestQueue requestQueue;

    private WebService(Context context, Certificate ca) {
        this.ca = ca;
        requestQueue = Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory()));
        serverResponse = new ServerResponse();
        this.context = context;
    }

    public static WebService getWebServiceInstance(Context context, Certificate ca) {
        if (instance == null) {
            instance = new WebService(context, ca);
        }
        return instance;
    }

    public static ServerResponse getCallbackInstance() {
        return serverResponse;
    }

    public void setCertificate(Certificate ca) {
        this.ca = ca;
    }

    public void sendFirebaseToken(String token){
        String path = "/testWholesaleToken";
        try{
            JSONObject headers = new JSONObject();
            headers.put("Authorization",token);
            headers.put("Content-Type","application/json");

            JSONObject body = new JSONObject();
            body.put("Authorization",token);

//            sendRequest2("POST", path, headers, body);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRequest(String requestMethod, String path, JSONObject headers, final JSONObject requestBody, ServerResponse serverResponse) {
        String BaseUrl="https://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com";
        String URL = BaseUrl + path;
        int method = 0;
        if (requestMethod.equals("POST")) {
            method = Request.Method.POST;
        } else if (requestMethod.equals("GET")) {
            method = Request.Method.GET;
        }
        Log.d(TAG, "sendRequest: headers ::: "+headers);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, URL, headers
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverResponse.responseReceived(response);
                Log.d(TAG, "onResponse: Response : "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: Error : "+error.getMessage());
                serverResponse.errorReceived(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type","application/json");
                try {
                    headerMap.put("Authorization", headers.getString("Authorization"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                return headerMap;
            }

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

    private SSLSocketFactory getSocketFactory() {

        CertificateFactory cf = null;
        try {

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo("ubuntu") == 0; //The Hostname of your server

                }
            };

            HostnameVerifier hostnameVerifierTEMP = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };


            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifierTEMP);
            SSLContext context = null;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            SSLSocketFactory sf = context.getSocketFactory();


            return sf;

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }

}
