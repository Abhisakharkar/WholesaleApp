package com.example.abhishek.wholesaleapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abhishek.wholesaleapp.Contract.SignUpContract;
import com.example.abhishek.wholesaleapp.Presenter.SignUpPresenter;
import com.example.abhishek.wholesaleapp.R;

public class SignUpActivity
        extends AppCompatActivity
        implements SignUpContract.View, TextView.OnEditorActionListener {

    private EditText mailEdittext, passEdittext, confirmPassEdittext;
    private Button signUpBtn;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mailEdittext = findViewById(R.id.signupactivity_mail_edittext);
        passEdittext = findViewById(R.id.signupactivity_pass_edittext);
        confirmPassEdittext = findViewById(R.id.signupactivity_confirm_pass_edittext);
        passEdittext.setOnEditorActionListener(this);

        presenter = new SignUpPresenter(this, this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText());
        return true;
    }

    public void loginLinkButtonOnClick(View view) {
        Intent gotoLoginIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        gotoLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(gotoLoginIntent);
    }

    public void SignUpButtonOnClick(View view) {
        presenter.signUp(mailEdittext.getText(), passEdittext.getText(), confirmPassEdittext.getText());
    }
}
