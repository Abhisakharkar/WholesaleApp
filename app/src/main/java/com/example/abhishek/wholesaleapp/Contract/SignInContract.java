package com.example.abhishek.wholesaleapp.Contract;

import android.text.Editable;

import com.example.abhishek.wholesaleapp.Enum.CredentialEnum;
import com.example.abhishek.wholesaleapp.Enum.SignInEnum;

public interface SignInContract {

     interface View{
         void showSnackbar(String message,int length);
         void switchActivity(SignInEnum result);
    }

     interface Presenter{
         void signIn(Editable mail, Editable pass);

         CredentialEnum validateFormData(Editable mail, Editable pass);

         void showValidatorMessage(CredentialEnum validationResult);

         void handleFirebaseException(Exception exception);
    }

}
