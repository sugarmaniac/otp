package com.example.phoneauth;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthRepo {
    private MutableLiveData<String> errorLiveData;
    private MutableLiveData<Boolean> isLoggedInLiveData;
    private MutableLiveData<String> loggedInPhone;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;

    public AuthRepo() {
        errorLiveData = new MutableLiveData<>();
        isLoggedInLiveData = new MutableLiveData<>();
        loggedInPhone = new MutableLiveData<>();
        firebaseAuth = FirebaseAuth.getInstance();
        checkAuthSituation();
        initCallback();
    }

    private void checkAuthSituation() {
        if(Objects.equals(firebaseAuth.getCurrentUser(), null)){
            isLoggedInLiveData.postValue(false);
            loggedInPhone.postValue(null);
        } else {
            isLoggedInLiveData.postValue(true);
            loggedInPhone.postValue(firebaseAuth.getCurrentUser().getPhoneNumber());
        }
    }


    private void initCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                errorLiveData.postValue(e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                mVerificationId = verificationId;
                forceResendingToken = token;
            }
        };
    }

    public void startPhoneNumberVerification(String phone, Activity activity){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void resendVerificationCode(String phone, Activity activity){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .setForceResendingToken(forceResendingToken)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyPhoneNumberWithCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    isLoggedInLiveData.postValue(true);
                    String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    loggedInPhone.postValue(phone);
                })
                .addOnFailureListener(e -> {
                    errorLiveData.postValue(e.getMessage());
                });
    }

    public void signOut(){
        FirebaseAuthU.signOut();
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public MutableLiveData<Boolean> getIsLoggedInLiveData() {
        return isLoggedInLiveData;
    }

    public MutableLiveData<String> getLoggedInPhone() {
        return loggedInPhone;
    }
}
