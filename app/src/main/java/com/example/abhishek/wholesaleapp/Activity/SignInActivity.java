package com.example.abhishek.wholesaleapp.Activity;

import android.content.Intent;
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

import com.example.abhishek.wholesaleapp.Contract.SignInContract;
import com.example.abhishek.wholesaleapp.Utils.Enum.SignInEnum;
import com.example.abhishek.wholesaleapp.Presenter.SignInPresenter;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.Model.NetworkUtils.WebService;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class SignInActivity extends AppCompatActivity implements SignInContract.View,TextView.OnEditorActionListener {

    private ConstraintLayout parentLayout;
    private EditText mailEdittext, passEdittext;
    private Button signInBtn;

    private SignInPresenter presenter;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        parentLayout = findViewById(R.id.signinactivity_parent_layout);
        mailEdittext = findViewById(R.id.signinactivity_mail_edittext);
        passEdittext = findViewById(R.id.signinactivity_pass_edittext);
        passEdittext.setOnEditorActionListener(this);
        //TEMP CODE ::: until implementation of DEPENDANCY INJECTION
        Certificate ca = null;
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = getResources().openRawResource(R.raw.myrootca);
            ca = cf.generateCertificate(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        WebService webService = WebService.getWebServiceInstance(this, ca);
        //TEMP CODE END

        presenter = new SignInPresenter(SignInActivity.this,this,webService);
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        return true;
    }
    public void SignInButtonOnClick(View view) {
        SignInEnum result=presenter.signIn(mailEdittext.getText(), passEdittext.getText());
        if (result== SignInEnum.EMAIL_NOT_VERIFIED){
            Intent gotoVerifyEmail = new Intent(SignInActivity.this, VerifyEmailActivity.class);
            gotoVerifyEmail.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(gotoVerifyEmail);
        }
        else if (result==SignInEnum.FAIL){
            //log and toast error
            Log.d(TAG, "SignInButtonOnClick: sign in error");

        }else if (result==SignInEnum.EMAIL_VERIFIED){
            //check for mandatory data and if filled go to home otherwise go to profile
            Log.d(TAG, "SignInButtonOnClick: email verify failed");

        }


    }

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

    public void signupLinkButtonOnClick(View view) {
        Intent gotosignupIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        gotosignupIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(gotosignupIntent);
    }

}
