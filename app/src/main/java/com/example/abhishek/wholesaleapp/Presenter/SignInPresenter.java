package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;
import com.example.abhishek.wholesaleapp.Contract.SignInContract;
import com.example.abhishek.wholesaleapp.Enum.CredentialEnum;
import com.example.abhishek.wholesaleapp.Enum.SignInEnum;
import com.example.abhishek.wholesaleapp.R;
import com.example.abhishek.wholesaleapp.SingletonClases.Firebase;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ResponseReceiveListener;
import com.example.abhishek.wholesaleapp.Utils.CustomCallbacks.ServerResponseCallback.ServerResponse;
import com.example.abhishek.wholesaleapp.Utils.NetworkUtils.WebService;
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

import static com.example.abhishek.wholesaleapp.Enum.CredentialEnum.EMAIL_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.CredentialEnum.EMAIL_WRONG_FORMAT;
import static com.example.abhishek.wholesaleapp.Enum.CredentialEnum.PASS_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.CredentialEnum.PASS_WRONG_FORMAT;

public class SignInPresenter implements SignInContract.Presenter,ResponseReceiveListener {
    private String TAG = "SignInPresenter";

    private SignInContract.View signInView;
    private Context context;
    public WebService webService;
    private ServerResponse serverResponse;
    public SignInPresenter(Context context, SignInContract.View signInView, WebService webService) {
        this.context = context;
        this.signInView = signInView;
//        webService = WebService.getWebServiceInstance(context.getApplicationContext());
        this.webService = webService;
        serverResponse = WebService.getCallbackInstance();
        serverResponse.setResponseReceiveListener(this);
    }
    @Override
    public SignInEnum signIn(Editable mail, Editable pass) {
        String email = mail.toString();
        String password = pass.toString();
        CredentialEnum validateResult = validateFormData(mail, pass);
        if (validateResult == CredentialEnum.OK) {
            Firebase firebase=Firebase.getInstance();
            if (firebase.signIn(email,password)){
                if (firebase.mailVerified()){
                   return SignInEnum.EMAIL_VERIFIED;
                } else{
                    return SignInEnum.EMAIL_NOT_VERIFIED;
                }

            }else
                return SignInEnum.FAIL;

        } else {
            showValidatorMessage(validateResult);
            return SignInEnum.FAIL;
        }

    }

    @Override
    public void showValidatorMessage(CredentialEnum validationResult) {
        int Snackbar_Length = Snackbar.LENGTH_SHORT;
        switch (validationResult) {
            case EMAIL_EMPTY:
                signInView.showSnackbar(context.getString(R.string.email_empty_message), Snackbar_Length);
                break;
            case PASS_EMPTY:
                signInView.showSnackbar(context.getString(R.string.pass_empty_string), Snackbar_Length);
                break;
            case EMAIL_WRONG_FORMAT:
                signInView.showSnackbar(context.getString(R.string.email_wrong_format_string), Snackbar_Length);
                break;
            case PASS_WRONG_FORMAT:
                signInView.showSnackbar(context.getString(R.string.pass_wrong_format_string), Snackbar_Length);
                break;
        }
    }

    @Override
    public CredentialEnum validateFormData(Editable mailEditable, Editable passEditable) {

        String mail = mailEditable.toString().trim();
        if (mail.isEmpty()) {
            return EMAIL_EMPTY;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            return EMAIL_WRONG_FORMAT;
        }

        String pass = passEditable.toString().trim();
        if (pass.isEmpty()) {
            return PASS_EMPTY;
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