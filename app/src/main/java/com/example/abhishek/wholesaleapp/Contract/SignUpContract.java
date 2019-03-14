package com.example.abhishek.wholesaleapp.Contract;

import android.text.Editable;

import com.example.abhishek.wholesaleapp.Enum.SignUpEnum;

public interface SignUpContract {

    public interface View {

        public void showSnackbar(String message,int length);

    }

    public interface Presenter {

        public void signUp(Editable mail, Editable pass, Editable confirmPass);

        public SignUpEnum validateFormData(Editable mail, Editable pass, Editable confirmPass);

        public void showValidatorMessage(SignUpEnum validationResult);
    }
}
