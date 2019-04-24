package com.example.abhishek.wholesaleapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.abhishek.wholesaleapp.Contract.VerifyEmailContract;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.SingletonClases.Firebase;

public class VerifyEmailActivity extends AppCompatActivity implements VerifyEmailContract.View {
        Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        firebase=Firebase.getInstance();
    }

    public void ResendBtnOnClick(View view){
        firebase.sendVerificationMail();

    }





    }
