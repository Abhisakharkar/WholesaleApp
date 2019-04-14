package com.example.abhishek.wholesaleapp.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
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
import com.example.abhishek.wholesaleapp.Utils.NetworkUtils.WebService;

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

        //TEMP CODE ::: until implementation of DEPENDANCY INJECTION
        Certificate ca = null;
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = getResources().openRawResource(R.raw.myrootca);
            ca = cf.generateCertificate(inputStream);
            presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText());

        } catch (Exception e) {
            e.printStackTrace();
        }
        WebService webService = WebService.getWebServiceInstance(this, ca);
        //TEMP CODE END

        presenter = new SignUpPresenter(SignUpActivity.this, this, webService);
    }

    //UI Methods
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

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

            presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText());

        } catch (Exception e) {
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

    //TEMP
    private SSLContext trustCert() throws CertificateException, IOException, KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException {
        AssetManager assetManager = getAssets();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(assetManager.open("myrootca.crt"));

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }


}
