package com.project.manishprajapat.letsmessage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.manishprajapat.letsmessage.databinding.ActivityVerificationBinding;

public class VerificationActivity extends AppCompatActivity {
    ActivityVerificationBinding verificationBinding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificationBinding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(verificationBinding.getRoot());

        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();

        verificationBinding.etAvMobile.requestFocus();
        progressDialog = new ProgressDialog(this);

        verificationBinding.buttonAvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!verificationBinding.etAvMobile.getText().toString().equals(" ") && !verificationBinding.etAvMobile.getText().toString().isEmpty()){
                    if(verificationBinding.etAvMobile.getText().toString().length()==10){

                        Intent intent = new Intent(getApplicationContext(), OTPActivity.class);
                        intent.putExtra("mobile",verificationBinding.etAvMobile.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    }
                    else{
//                        verificationBinding.etAvMobile.setError("Error");
                        Toast.makeText(VerificationActivity.this, "Enter 10 length mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
//                    verificationBinding.etAvMobile.setError("Error");
                    Toast.makeText(VerificationActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




//        verificationBinding.buttonAvSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String mobile = verificationBinding.etAvMobile.getText().toString();
//
//                if (!mobile.isEmpty()) {
//                    if (mobile.length() == 10) {
//
//                        //OTP fetching dialog
//                        dialog.setMessage("OTP Sending...");
////                        dialog.setCancelable(false);
//                        dialog.show();
//
//                        getOtp();
//                    }
//                else {
//                        verificationBinding.etAvMobile.setError("Enter valid mobile number");
//                    }
//                }
//                else {
//                    verificationBinding.etAvMobile.setError("Enter mobile number");
//                }
//            }
//        });
//
//    }
//
//    private void getOtp() {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber("+91" + verificationBinding.etAvMobile.getText().toString().trim())
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                // This callback will be invoked in two situations:
//                                // 1 - Instant verification. In some cases the phone number can be instantly
//                                //     verified without needing to send or enter a verification code.
//                                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                                //     detect the incoming verification SMS and perform verification without
//                                //     user action.
//
//                                dialog.dismiss();
//
//
//                            }
//
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                // This callback is invoked in an invalid request for verification is made,
//                                // for instance if the the phone number format is not valid.
//                                Log.d(TAG, "onVerificationFailed: " + e);
//                                dialog.dismiss();
//
//                                if (e instanceof FirebaseAuthInvalidCredentialsException)
//                                    Toast.makeText(VerificationActivity.this, "", Toast.LENGTH_SHORT).show();
//                                if (e instanceof FirebaseTooManyRequestsException)
//                                    Toast.makeText(VerificationActivity.this, "SMS limit exceed", Toast.LENGTH_SHORT).show();
//                                else
//                                    Toast.makeText(VerificationActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
//
//                            }
//                            @Override
//                            public void onCodeSent(@NonNull String verifiedId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                super.onCodeSent(verifiedId, forceResendingToken);
//
//                                // The SMS verification code has been sent to the provided phone number, we
//                                // now need to ask the user to enter the code and then construct a credential
//                                // by combining the code with a verification ID.
//
//                                Log.d(TAG, "onCodeSent: " + verifiedId);
//                                dialog.dismiss();
//
//                                mVerifiedID = verifiedId;
//                                mforceResendingToken = forceResendingToken;
//
//
//
//
//                                // Save verification ID and resending token so we can use them later
//
//
//                            }
//                        })
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        finish();
    }
}