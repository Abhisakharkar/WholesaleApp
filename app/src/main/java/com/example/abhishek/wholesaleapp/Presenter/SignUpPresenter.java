package com.example.abhishek.wholesaleapp.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;

import com.example.abhishek.wholesaleapp.Contract.SignUpContract;

import com.example.abhishek.wholesaleapp.Enum.SignUpEnum;
import com.example.abhishek.wholesaleapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.regex.Pattern;

import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.EMAIL_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.EMAIL_WRONG_FORMAT;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_EMPTY;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_NOT_SAME;
import static com.example.abhishek.wholesaleapp.Enum.SignUpEnum.PASS_WRONG_FORMAT;

public class SignUpPresenter implements SignUpContract.Presenter {

    private String TAG = "SignUpPresenter";

    private SignUpContract.View signUpView;
    private Context context;

    public SignUpPresenter(Context context, SignUpContract.View signUpView) {
        this.context = context;
        this.signUpView = signUpView;
    }


    @Override
    public void signUp(Editable mail, Editable pass, Editable confirmPass) {
        String email = mail.toString();
        String password = pass.toString();
        SignUpEnum validateResult = validateFormData(mail, pass, confirmPass);
        if (validateResult == SignUpEnum.OK) {

            FirebaseAuth
                    .getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            //sign up successfull
                            //TODO send verification email
                            //TODO save user data
                            //TODO goto profile activity
                        } else {
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
































