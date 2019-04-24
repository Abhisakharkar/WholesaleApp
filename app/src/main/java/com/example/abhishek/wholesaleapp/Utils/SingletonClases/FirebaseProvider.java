package com.example.abhishek.wholesaleapp.Utils.SingletonClases;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseProvider {
    private static FirebaseProvider mInstance;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;


    private FirebaseProvider() {
        mInstance = this;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized FirebaseProvider getInstance() {
        // If instance is not available, create it. If available, reuse and return the object.
        if (mInstance == null) {
            mInstance = new FirebaseProvider();
        }
        return mInstance;
    }

    public FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public FirebaseUser getUser() {
        if (mUser == null) {
            mUser = firebaseAuth.getCurrentUser();
        }
        return mUser;
    }


    //Below methods are in FirebaseHelper

//
//
//    public boolean signIn(String email, String password, FirebaseAuthCallback callback) {
//        firebaseAuth
//                .signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener((task) -> {
//                    if (task.isSuccessful()) {
//                        mUser = firebaseAuth.getCurrentUser();
//                        //sign in successfull
//                        callback.onSuccess();
//                    } else {
//                        Log.e(TAG, "signIn: " + task.getException());
//                        callback.onFailure();
//                        // signInView.showSnackbar("Error : See Logs", Snackbar.LENGTH_LONG);
//                        // handleFirebaseException(task.getException());
//                    }
//                });
//
//        return true;
//    }
//
//    public String getTokenFromFirebase() {
//        if (mUser != null) {
//            mUser.getIdToken(true)
//                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                        public void onComplete(@NonNull Task<GetTokenResult> task) {
//                            if (task.isSuccessful()) {
//                                token = task.getResult().getToken();
//                                Log.d(TAG, "onComplete: token  " + token);
//                            } else {
//                                // Handle error -> task.getException();
//                                Log.e(TAG, "onComplete: Error : " + task.getException());
//                            }
//                        }
//                    });
//        }
//        return token;
//    }

//    public boolean mailVerified() {
//        return mUser.isEmailVerified();
//    }


}
