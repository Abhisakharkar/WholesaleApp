package com.example.abhishek.wholesaleapp.Contract;

import android.text.Editable;
import android.widget.EditText;

public interface SignUpContract {

    public interface View {

        public void showSnackbar(String message);

    }

    public interface Presenter {

        public void signUp(Editable mail, Editable pass, Editable confirmPass);

    }
}
