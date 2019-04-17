package com.example.abhishek.wholesaleapp.Contract;

import android.text.Editable;

import com.example.abhishek.wholesaleapp.Enum.SignUpEnum;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.security.cert.Certificate;

public interface SignInContract {

    public interface View{
        public void showSnackbar(String message,int length);
    }

    public interface Presenter{
        public void signIn(Editable mail, Editable pass);

        public SignUpEnum validateFormData(Editable mail, Editable pass);

        public void showValidatorMessage(SignUpEnum validationResult);

        public void handleFirebaseException(Exception exception);
    }

}
