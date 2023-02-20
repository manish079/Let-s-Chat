package com.project.manishprajapat.letsmessage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding resetPasswordBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetPasswordBinding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(resetPasswordBinding.getRoot());

        getSupportActionBar().hide();



        resetPasswordBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resetPasswordBinding.etRpPass.getText().toString().trim().isEmpty()){
                    resetPasswordBinding.etRpPass.setError("Password");
                }
                else if(resetPasswordBinding.etRsConPass.getText().toString().trim().isEmpty()){
                    resetPasswordBinding.etRpPass.setError("Confirm Password");
                }
                else{

                    Intent intent = new Intent(getApplicationContext(), PasswordUpdateActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}