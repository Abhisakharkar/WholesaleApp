package com.example.abhishek.wholesaleapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abhishek.wholesaleapp.Contract.SignInContract;
import com.example.abhishek.wholesaleapp.R;

public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}
