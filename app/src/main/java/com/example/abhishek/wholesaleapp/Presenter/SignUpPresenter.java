package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;
import com.example.abhishek.wholesaleapp.Contract.SignUpContract;

import com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ResponseReceiveListener;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;
import com.example.abhishek.wholesaleapp.Model.NetworkUtils.WebService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.security.cert.CertPathValidatorException;
import java.util.regex.Pattern;

import static com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum.EMAIL_EMPTY;
import static com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum.EMAIL_WRONG_FORMAT;
import static com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum.PASS_EMPTY;
import static com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum.PASS_NOT_SAME;
import static com.example.abhishek.wholesaleapp.Utils.Enum.CredentialEnum.PASS_WRONG_FORMAT;

public class SignUpPresenter implements SignUpContract.Presenter, ResponseReceiveListener {

    private String TAG = "SignUpPresenter";

    private SignUpContract.View signUpView;
    private Context context;
    public WebService webService;
    private ServerResponse serverResponse;

    public SignUpPresenter(Context context, SignUpContract.View signUpView, WebService webService) {
        this.context = context;
        this.signUpView = signUpView;
//        webService = WebService.getWebServiceInstance(context.getApplicationContext());
        this.webService = webService;
        serverResponse = WebService.getCallbackInstance();
        serverResponse.setResponseReceiveListener(this);
    }


    @Override
    public void signUp(Editable mail, Editable pass, Editable confirmPass) {
        String email = mail.toString();
        String password = pass.toString();
        CredentialEnum validateResult = validateFormData(mail, pass, confirmPass);
        if (validateResult == CredentialEnum.OK) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {

                            //TEMPORARY CODE --- TO CHECK SERVER INTEGRATION
                            //Log.d(TAG, "Token : "+task.getResult().getUser().getIdToken(true).toString());
                            FirebaseUser mUser = firebaseAuth.getCurrentUser();
                            mUser.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                String token = task.getResult().getToken();
                                                Log.d(TAG, "onComplete: token  " + token);

                                                webService.sendFirebaseToken(token);

                                            } else {
                                                // Handle error -> task.getException();
                                                Log.e(TAG, "onComplete: Error : " + task.getException());
                                            }
                                        }
                                    });


                            //sign up successfull
                            //TODO send verification email

                            task.getResult().getUser().sendEmailVerification()
                                    .addOnCompleteListener((verifyTask) -> {
                                        if (verifyTask.isSuccessful()) {
                                            Log.d(TAG, "signUp: verification emiail sent successfully !");
                                        } else {
                                            Log.e(TAG, "signUp: send verification email error\n" + verifyTask.getException().getMessage());
                                        }
                                    });
                            //TODO save user data
                            //TODO goto profile activity
                        } else {
                            //TODO remove below 2 lines after debugging
                            Log.e(TAG, "signUp: " + task.getException());
                            signUpView.showSnackbar("Error : See Logs", Snackbar.LENGTH_LONG);

                            handleFirebaseException(task.getException());

                        }
                    });

        } else {
            showValidatorMessage(validateResult);
        }
    }

    @Override
    public void showValidatorMessage(CredentialEnum validationResult) {
        int Snackbar_Length = Snackbar.LENGTH_SHORT;
        switch (validationResult) {
            case EMAIL_EMPTY:
                signUpView.showSnackbar(context.getString(R.string.email_empty_message), Snackbar_Length);
                break;
            case PASS_EMPTY:
                signUpView.showSnackbar(context.getString(R.string.pass_empty_string), Snackbar_Length);
                break;
            case EMAIL_WRONG_FORMAT:
                signUpView.showSnackbar(context.getString(R.string.email_wrong_format_string), Snackbar_Length);
                break;
            case PASS_NOT_SAME:
                signUpView.showSnackbar(context.getString(R.string.pass_not_same_string), Snackbar_Length);
                break;
            case PASS_WRONG_FORMAT:
                signUpView.showSnackbar(context.getString(R.string.pass_wrong_format_string), Snackbar_Length);
                break;
        }
    }

    @Override
    public CredentialEnum validateFormData(Editable mailEditable, Editable passEditable, Editable confirmPassEditable) {

        String mail = mailEditable.toString().trim();
        if (mail.isEmpty()) {
            return EMAIL_EMPTY;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            return EMAIL_WRONG_FORMAT;
        }

        String pass = passEditable.toString().trim();
        String confirmPass = confirmPassEditable.toString().trim();

        if (pass.isEmpty() || confirmPass.isEmpty()) {
            return PASS_EMPTY;
        }
        if (!pass.equals(confirmPass)) {
            return PASS_NOT_SAME;
        }

        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");
        if (!PASSWORD_PATTERN.matcher(pass).matches()) {
            return PASS_WRONG_FORMAT;
        }
        return CredentialEnum.OK;
    }

    @Override
    public void handleFirebaseException(Exception exception) {
        //TODO handle exceptions
        try {
            throw exception;
        } catch (FirebaseAuthActionCodeException e) {
            //no sub parsing avaibale
        } catch (FirebaseAuthEmailException e) {
            //not relavent ---> verify and remove
        } catch (FirebaseAuthInvalidCredentialsException e) {
            //
        } catch (FirebaseAuthInvalidUserException e) {
        } catch (FirebaseAuthRecentLoginRequiredException e) {
        } catch (FirebaseAuthUserCollisionException e) {
        } catch (Exception e) {
            Log.e(TAG, "handleFirebaseException: \n" + e.getMessage());
        }
    }

    @Override
    public void onResponseReceive(JSONObject responseObject) {
        Log.d(TAG, "onResponseReceive: Response : "+responseObject.toString());
        //TODO process this response
    }

    @Override
    public void onErrorReceive(VolleyError error) {
        Log.e(TAG, "onErrorReceive: Error : " + error.getMessage());
        Log.e(TAG, "onErrorReceive: Error cause : " + error.getCause() );
        try{
            throw new CertPathValidatorException(error);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
































