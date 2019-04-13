package com.example.abhishek.wholesaleapp.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhishek.wholesaleapp.Contract.SignUpContract;
import com.example.abhishek.wholesaleapp.Presenter.SignUpPresenter;
import com.example.abhishek.wholesaleapp.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

public class SignUpActivity
        extends AppCompatActivity
        implements SignUpContract.View, TextView.OnEditorActionListener {

    private ConstraintLayout parentLayout;
    private EditText mailEdittext, passEdittext, confirmPassEdittext;
    private Button signUpBtn;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        parentLayout = findViewById(R.id.signupactivity_parent_layout);
        mailEdittext = findViewById(R.id.signupactivity_mail_edittext);
        passEdittext = findViewById(R.id.signupactivity_pass_edittext);
        confirmPassEdittext = findViewById(R.id.signupactivity_confirm_pass_edittext);
        passEdittext.setOnEditorActionListener(this);

        presenter = new SignUpPresenter(SignUpActivity.this, this);
    }

    //UI Methods
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = getResources().openRawResource(R.raw.myrootca);
            Certificate ca = cf.generateCertificate(inputStream);
            presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText(),ca);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public void loginLinkButtonOnClick(View view) {
        Intent gotoLoginIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        gotoLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(gotoLoginIntent);
    }

    public void SignUpButtonOnClick(View view) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = getResources().openRawResource(R.raw.myrootca);
            Certificate ca = cf.generateCertificate(inputStream);

            //CLOSE INPUT STREAM PROPERLY ::::::::: MEM LEAK
            inputStream.close();

            presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText(), ca);

        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    //Contract Methods
    @Override
    public void showSnackbar(String message, int length) {
        final Snackbar snackbar = Snackbar.make(parentLayout, message, length);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

//
//    //TEMP METHOD
//    public void temp() {
//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = getResources().openRawResource(R.raw.myrootca);
//            Certificate ca;
//            ca = cf.generateCertificate(caInput);
//            caInput.close();
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//            URL url = new URL("url");
//            HttpsURLConnection urlConnection =
//                    (HttpsURLConnection) url.openConnection();
////                urlConnection.setSSLSocketFactory(getSocketFactory());
//            InputStream in = urlConnection.getInputStream();
////                copyInputStreamToOutputStream(in, System.out);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void sendSSLRequest() {
//      //  RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://ec2-13-234-45-216.ap-south-1.compute.amazonaws.com:443";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("SSL Response", "onResponse: " + response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley SSL Error", "onErrorResponse: " + error.toString());
//                Log.e("Volley SSL Error", "onErrorResponse: message : " + error.getMessage());
//            }
//        });
//        requestQueue.add(stringRequest);
//    }

//    private SSLSocketFactory getSocketFactory() {
//
//        CertificateFactory cf = null;
//        try {
//            cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = getResources().openRawResource(R.raw.myrootca);
//            Certificate ca;
//            try {
//                ca = cf.generateCertificate(caInput);
//                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//
//            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//
//                    Log.e("CipherUsed", session.getCipherSuite());
//                    return hostname.compareTo("ubuntu") == 0; //The Hostname of your server
//
//                }
//            };
//
//
////            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);/
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//
//            context.init(null, tmf.getTrustManagers(), null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            SSLSocketFactory sf = context.getSocketFactory();
//
//
//            return sf;
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

}
