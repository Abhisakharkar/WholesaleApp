package com.example.abhishek.wholesaleapp.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.abhishek.wholesaleapp.R;

public class SuccessfulEmailActivity extends AppCompatActivity {
    private Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_email);
    }
    public void loginButtonOnClick(View view) {
        Intent gotoLoginIntent = new Intent(SuccessfulEmailActivity.this, SignInActivity.class);
        gotoLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(gotoLoginIntent);
    }
}
