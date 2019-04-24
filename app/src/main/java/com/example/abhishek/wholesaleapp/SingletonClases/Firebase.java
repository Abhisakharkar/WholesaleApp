package com.example.abhishek.wholesaleapp.SingletonClases;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.Callback.SuccessAndFailureCallback;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.Callback.SuccessCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import static android.content.ContentValues.TAG;

public class Firebase {
    private static Firebase mInstance;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private String token;
    private boolean result = false;

    private Firebase() {
        mInstance = this;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized Firebase getInstance() {
        // If instance is not available, create it. If available, reuse and return the object.
        if (mInstance == null) {
            mInstance = new Firebase();
        }
        return mInstance;
    }

    public void signIn(String email, String password, SuccessCallback callback) {
        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        mUser = firebaseAuth.getCurrentUser();
                        //sign in successfull
                        callback.onSuccess();
                    } else {
                        Log.e(TAG, "signIn: " + task.getException());
                        // signInView.showSnackbar("Error : See Logs", Snackbar.LENGTH_LONG);
                        // handleFirebaseException(task.getException());
                    }
                });
    }

    public String getTokenFromFirebase() {
        if (mUser != null) {
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                token = task.getResult().getToken();
                                Log.d(TAG, "onComplete: token  " + token);
                            } else {
                                // Handle error -> task.getException();
                                Log.e(TAG, "onComplete: Error : " + task.getException());
                            }
                        }
                    });
        }
        return token;
    }

    public FirebaseUser getUser() {
        if (mUser == null) {
            mUser = firebaseAuth.getCurrentUser();
        }
        return mUser;
    }

    public boolean mailVerified() {
        if (mUser!=null)
        return mUser.isEmailVerified();
        else
            return false;
    }

    public void sendVerificationMail(){
        if (mUser == null) {
            mUser = firebaseAuth.getCurrentUser();
        }
        mUser.sendEmailVerification();
    }


}
