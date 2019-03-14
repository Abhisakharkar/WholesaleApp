package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.util.Log;

import com.example.abhishek.wholesaleapp.Contract.SignUpContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenter implements SignUpContract.Presenter {

    private String TAG = "SignUpPresenter";

    private SignUpContract.View signUpView;
    private Context context;

    public SignUpPresenter(Context context, SignUpContract.View signUpView) {
        this.context = context;
        this.signUpView = signUpView;
    }


    @Override
    public void signUp(Editable mail, Editable pass, Editable confirmPass) {
        if (mail.toString().isEmpty() || pass.toString().isEmpty() || confirmPass.toString().isEmpty()) {
            signUpView.showSnackbar("Complete sign up form !");
        } else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(mail.toString(), pass.toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: " + task.getResult().getUser().getUid());
                            } else {
                                Log.e(TAG, "onComplete: " + task.getException());
                            }
                        }
                    });
        }
    }
}
