package com.example.phoneauth;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class viewModel extends ViewModel {
    private AuthRepo authRepo;

    public viewModel() {
        authRepo = new AuthRepo();
    }

    public MutableLiveData<String> getErrorLiveData() {
        return authRepo.getErrorLiveData();
    }

    public MutableLiveData<Boolean> getIsLoggedInLiveData() {
        return authRepo.getIsLoggedInLiveData();
    }

    public MutableLiveData<String> getLoggedInPhone() {
        return authRepo.getLoggedInPhone();
    }

    public void startPhoneNumberVerification(String phoneNumber, MainActivity mainActivity) {
        authRepo.startPhoneNumberVerification(phoneNumber, mainActivity);
    }

    public void verifyPhoneNumberWithCode(String code) {
        authRepo.verifyPhoneNumberWithCode(code);
    }

    public void resendVerificationCode(String phone, Activity activity){
        authRepo.resendVerificationCode(phone, activity);
    }
}
