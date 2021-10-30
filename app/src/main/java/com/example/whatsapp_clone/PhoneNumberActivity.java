package com.example.whatsapp_clone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp_clone.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        getSupportActionBar().hide();

        binding.phoneBox.requestFocus();
        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(PhoneNumberActivity.this);
                dialog.setMessage("Sending OTP...");
                dialog.setCancelable(false);
                dialog.show();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + binding.phoneBox.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        PhoneNumberActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {}



                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {}

                            @Override
                            public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifyId, forceResendingToken);
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                                Intent intent1 = intent.putExtra("phoneNumber", binding.phoneBox.getText().toString());
                                intent.putExtra("backendCode", verifyId);
                                startActivity(intent);
                            }


                        }
                );
            }
        });}}