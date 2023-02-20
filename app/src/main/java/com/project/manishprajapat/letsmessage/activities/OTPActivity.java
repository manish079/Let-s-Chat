package com.project.manishprajapat.letsmessage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.databinding.ActivityOtpactivityBinding;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class OTPActivity extends AppCompatActivity {

    private final static String TAG = "phoneAuthActivity";
    ActivityOtpactivityBinding otpactivityBinding;
    FirebaseAuth firebaseAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendingToken;
    public static boolean isTimeOut = false;
    ProgressDialog progressDialog;
//    private String mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpactivityBinding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(otpactivityBinding.getRoot());

        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //get otp mobile number from verification activity
        Intent intent = getIntent();
        String mobile_no = intent.getStringExtra("mobile");

        otpactivityBinding.tvText.setText("We send verification code to your \n email " + "+91-" + mobile_no + ". You can \n check your inbox");


//        1. Generate OTP

        //If SIM in same device

        initiateOTP(mobile_no);  //calling three callbacks

//        otpactivityBinding.otpView.setOtpListener(new OTPListener() {
//            @Override
//            public void onInteractionListener() {
//
//            }
//
//            @Override
//            public void onOTPComplete(String otp) {
//                signInWithPhoneAuthCredential(mVerificationId, otp);
//            }
//        });


        otpactivityBinding.btnVerifiedOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpactivityBinding.otpView.getOTP().isEmpty()){

                    Toast.makeText(getApplicationContext(), "Please enter OTP!!", Toast.LENGTH_SHORT).show();
                }
                else if(otpactivityBinding.otpView.getOTP().length() != 6){
                    Toast.makeText(getApplicationContext(), "Invalid OTP!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpactivityBinding.otpView.getOTP());
                    signInWithPhoneAuthCredential(credential);

//                    verifyPhoneNumberWithCode(mVerificationId, otpactivityBinding.otpView.getOTP());
                }

            }
        });
    }

//    public static void verifyPhoneNumberWithCode(String mVerificationId,String otp){
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
//        signInWithPhoneAuthCredential(credential);
//    }

    private void initiateOTP(String mobile_no) {

        progressDialog.setMessage("Sending OTP...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+ mobile_no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //This called SIM In our phone
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            progressDialog.dismiss();

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        //This called when number not in our phone
        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            progressDialog.dismiss();

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendingToken = token;
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
                             finish();
                             Intent intent = new Intent();
                             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // clear otp crediendial
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(OTPActivity.this, "signing failure", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }




//        otpactivityBinding.btnVerifiedOtp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//               if(firebase_send_otp==null){
//                   Toast.makeText(OTPActivity.this, "Internet Connection!", Toast.LENGTH_SHORT).show();
//               }
//
//                if (!otpactivityBinding.otpView.getOTP().isEmpty() && otpactivityBinding.otpView.getOTP().length() == 6) {
//                    //verify otp successfully
//                    String otp1 = otpactivityBinding.otpView.getOTP();
////                    if(otp1 != firebase_send_otp){
////                        Toast.makeText(OTPActivity.this, "Enter correct OTP!", Toast.LENGTH_SHORT).show();
////                    }
////                    else{
//
////                        Toast.makeText(OTPActivity.this, "OTP Verified!", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(OTPActivity.this, ResetPasswordActivity.class);
////                        startActivity(intent);
////                        finish();
//                        otpactivityBinding.otpView.setOtpListener(new OTPListener() {
//                            @Override
//                            public void onInteractionListener() {
//                                Toast.makeText(OTPActivity.this, "False", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onOTPComplete(String otp) {
//                                //otp length correct
//                                Toast.makeText(OTPActivity.this, "True", Toast.LENGTH_SHORT).show();
//                                signInWithPhoneAuthCredential();
////                otpactivityBinding.otpView.setOTP(" ");
//                            }
//
//                        });
//
////                    }
//                } else {
//                    Toast.makeText(OTPActivity.this, "Enter 6 digit OTP..", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



//        otpactivityBinding.otpView.setOtpListener(new OTPListener() {
//            @Override
//            public void onInteractionListener() {
//                Toast.makeText(OTPActivity.this, "False", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onOTPComplete(String otp) {
//                //otp length correct
//                Toast.makeText(OTPActivity.this, "True", Toast.LENGTH_SHORT).show();
//                verifyPhoneNumberWithCode(mVerificationId,otp);
////                otpactivityBinding.otpView.setOTP(" ");
//            }
//
//        });

//        otpactivityBinding.tvSendAgain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                resendVerificationCode(mobile_no, mResendingToken);
//                Toast.makeText(OTPActivity.this, "Resend Code...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        updateUI(currentUser);
//    }


        // [START resend_verification]
//    private void resendVerificationCode(String phoneNumber,
//                                        PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(firebaseAuth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Log.d(TAG, "onVerificationFailed: " + e);
//
//                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                                    // Invalid request
//                                } else if (e instanceof FirebaseTooManyRequestsException) {
//                                    // The SMS quota for the project has been exceeded
//                                }
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken nforceResendingToken) {
//                                super.onCodeSent(newVerificationId, nforceResendingToken);
//                                Log.d(TAG, "onCodeSent: " + newVerificationId);
//                                progressDialog.dismiss();
//                                mVerificationId = newVerificationId;
//                                mResendingToken = nforceResendingToken;
//                                Toast.makeText(OTPActivity.this, "Again OTP send..", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//
//                        .setForceResendingToken(mResendingToken)     // ForceResendingToken from callbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }

//    }



//    private void signInWithPhoneAuthCredential() {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpactivityBinding.otpView.getOTP());
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    // Sign in success, update UI with the signed-in user's
//                    Log.d(TAG, "signInWithCredential:success");
//                    Toast.makeText(OTPActivity.this, "Successfully you can register password", Toast.LENGTH_SHORT).show();
//
//                } else {
////                    Sign in failed, display a message and update the UI
//                    Log.w(TAG, "Failed", task.getException());
//                }
//            }
//        });
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}