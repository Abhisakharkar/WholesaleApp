package com.example.abhishek.wholesaleapp.Utils.FirebaseUtils;

import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.FirebaseCallbacks.FirebaseAuthCallback;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.UniversalCallbacks.JsonDataCallback;
import com.example.abhishek.wholesaleapp.Utils.SingletonClases.FirebaseProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseHelper {

    private FirebaseProvider firebaseProvider;

    public FirebaseHelper() {
        firebaseProvider = FirebaseProvider.getInstance();
    }

    public void signIn(String mail, String pass, FirebaseAuthCallback callback) {
        firebaseProvider.getFirebaseAuth().signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    public void signUp(String mail, String pass, FirebaseAuthCallback callback) {
        firebaseProvider.getFirebaseAuth().createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    public void getFirebaseToken(JsonDataCallback callback) {
        firebaseProvider.getUser().getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("from", "getFirebaseToken");
                            jsonObject.put("token", token);
                            callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(null);
                        }
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("from", "getFirebaseToken");
                            jsonObject.put("error", task.getException());

                            callback.onFailure(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(null);
                        }
                    }
                });
    }

    public boolean isMailVerified() {
        return firebaseProvider.getUser().isEmailVerified();
    }

}
