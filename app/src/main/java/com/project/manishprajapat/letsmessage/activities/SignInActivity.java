package com.project.manishprajapat.letsmessage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.project.manishprajapat.letsmessage.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding activitySignInBinding;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    public static final String TAG = "signin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(activitySignInBinding.getRoot());


        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("SignIn...");

        activitySignInBinding.tvSiGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInCheck();
            }
        });


        activitySignInBinding.buttonSiSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(activitySignInBinding.etSiEmail.getText().toString().trim())){
                    activitySignInBinding.etSiEmail.setError("Empty");
                }
                else if(TextUtils.isEmpty(activitySignInBinding.etSiPassword.getText().toString())){
                    activitySignInBinding.etSiPassword.setError("Empty");
                }
                if(activitySignInBinding.etSiPassword.getText().length() < 6 ){
                    Toast.makeText(SignInActivity.this, "Invalid password length!!", Toast.LENGTH_SHORT).show();
                }
                else if(activitySignInBinding.etSiEmail.getText().toString().matches("\\b[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}\\b")){
                    progressDialog.show();
                    checkUserSignIn();
                }
                else{
                    activitySignInBinding.etSiEmail.setError("Not valid email");
                }

            }
        });


        activitySignInBinding.tvDontHAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivities.class);
                startActivity(intent);
                finish();
            }
        });

        activitySignInBinding.tvSiFPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, VerificationActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void googleSignInCheck() {


    }

    private void checkUserSignIn() {
        String email = activitySignInBinding.etSiEmail.getText().toString().trim();
        String password = activitySignInBinding.etSiPassword.getText().toString().trim();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                    finish();
                    Log.d(TAG, "signInWithEmailAndPassword: "+"User Successfully signIN");
                }
                else{
                    Toast.makeText(SignInActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //if user already signIN that by default goes on MainDashboard activity
    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
            finish();
        }

    }
}