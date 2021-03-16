package com.example.phoneauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.phoneauth.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private viewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel = new ViewModelProvider(this).get(viewModel.class);
        initButtons();
        initObservers();
    }

    private void initButtons() {
        binding.sendPhone.setOnClickListener(v -> {
            String phoneNumber = getPhoneNumber();
            mViewModel.startPhoneNumberVerification(phoneNumber, this);
        });

        binding.verify.setOnClickListener(v -> {
            String code = getVerificationCode();
            mViewModel.verifyPhoneNumberWithCode(code);
        });
        binding.resend.setOnClickListener(v -> {
            String phoneNumber = getPhoneNumber();
            mViewModel.resendVerificationCode(phoneNumber, this);
        });
    }

    private String getPhoneNumber() {
        return binding.phoneNumber.getText().toString();
    }

    private String getVerificationCode() {
        return binding.code.getText().toString();
    }

    private void initObservers() {
        mViewModel.getErrorLiveData().observe(this, error -> {
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
        mViewModel.getIsLoggedInLiveData().observe(this, isLoggedIn ->{
            if(isLoggedIn){
//                Toast.makeText(this, "logged In", Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getLoggedInPhone().observe(this, phoneNumber -> {
//            Toast.makeText(this , phoneNumber , Toast.LENGTH_SHORT).show();
        });
    }
}
