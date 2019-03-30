package com.example.abhishek.wholesaleapp.Utils.NetworkUtils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class WebService {

    public static WebService instance;
    public static ServerResponse serverResponse;
    private Context context;

    private RequestQueue requestQueue;

    private WebService(Context context) {
        requestQueue = Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory()));
        serverResponse = new ServerResponse();
        this.context = context;
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

    public void temporaryMethodToCheckSSLwithServer(String token, Certificate ca){
        String path = "/testWholesaleToken";
        try{
            JSONObject headers = new JSONObject();
            headers.put("Authorization",token);

            sendRequest("POST", path, headers, null);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private SSLSocketFactory getSocketFactory() {

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().openRawResource(R.raw.myrootca);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }


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


//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);/
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
