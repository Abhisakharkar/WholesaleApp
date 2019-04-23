package com.example.abhishek.wholesaleapp.Contract;

import android.text.Editable;

import com.example.abhishek.wholesaleapp.Enum.CredentialEnum;

public interface SignUpContract {

     interface View {

         void showSnackbar(String message,int length);

    }

     interface Presenter {

         void signUp(Editable mail, Editable pass, Editable confirmPass);

         CredentialEnum validateFormData(Editable mail, Editable pass, Editable confirmPass);

         void showValidatorMessage(CredentialEnum validationResult);

         void handleFirebaseException(Exception exception);
    }
}
