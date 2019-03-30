package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhishek.wholesaleapp.Contract.SignUpContract;

import com.example.abhishek.wholesaleapp.Enum.SignUpEnum;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;
import com.example.abhishek.wholesaleapp.Utils.NetworkUtils.WebService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.EMAIL_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.EMAIL_WRONG_FORMAT;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_NOT_SAME;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_WRONG_FORMAT;

public class SignUpPresenter implements SignUpContract.Presenter {

    private String TAG = "SignUpPresenter";

    private SignUpContract.View signUpView;
    private Context context;
    private WebService webService;
    private ServerResponse serverResponse;

    public SignUpPresenter(Context context, SignUpContract.View signUpView) {
        this.context = context;
        this.signUpView = signUpView;
        webService = WebService.getWebServiceInstance(context.getApplicationContext());
        serverResponse = WebService.getCallbackInstance();
    }


    @Override
    public void signUp(Editable mail, Editable pass, Editable confirmPass, Certificate ca) {
        String email = mail.toString();
        String password = pass.toString();
        SignUpEnum validateResult = validateFormData(mail, pass, confirmPass);
        if (validateResult == SignUpEnum.OK) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {

                            //TEMPORARY CODE --- TO CHECK SERVER INTEGRATION
                            Log.d(TAG, "Token : "+task.getResult().getUser().getIdToken(false).toString());
                            webService.temporaryMethodToCheckSSLwithServer(task.getResult().getUser().getIdToken(false).toString(),ca);

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
    public void showValidatorMessage(SignUpEnum validationResult) {
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
    public SignUpEnum validateFormData(Editable mailEditable, Editable passEditable, Editable confirmPassEditable) {

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
        return SignUpEnum.OK;
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
}
































