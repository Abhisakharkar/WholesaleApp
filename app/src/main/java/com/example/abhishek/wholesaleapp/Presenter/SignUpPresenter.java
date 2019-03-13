package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.text.Editable;

import com.example.abhishek.wholesaleapp.Contract.SignUpContract;

public class SignUpPresenter implements SignUpContract.Presenter {

    private SignUpContract.View signUpView;
    private Context context;

    public SignUpPresenter(Context context, SignUpContract.View signUpView){
        this.context = context;
        this.signUpView = signUpView;
    }


    @Override
    public void signUp(Editable mail, Editable pass, Editable confirmPass) {

    }
}
