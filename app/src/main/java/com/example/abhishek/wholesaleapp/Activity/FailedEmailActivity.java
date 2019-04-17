package com.example.abhishek.wholesaleapp.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.abhishek.wholesaleapp.R;

public class FailedEmailActivity extends AppCompatActivity {
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_email);
    }
    public void signUpButtonOnClick(View view) {
        Intent gotoSignupIntent = new Intent(FailedEmailActivity.this, SignUpActivity.class);
        gotoSignupIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(gotoSignupIntent);
    }
}
