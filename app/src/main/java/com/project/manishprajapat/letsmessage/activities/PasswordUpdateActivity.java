package com.project.manishprajapat.letsmessage.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.databinding.ActivityPasswordUpdateBinding;

public class PasswordUpdateActivity extends AppCompatActivity {
    ActivityPasswordUpdateBinding updateBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateBinding = ActivityPasswordUpdateBinding.inflate(getLayoutInflater());
        setContentView(updateBinding.getRoot());

        getSupportActionBar().hide();



        updateBinding.buttonPuSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        return super.onSupportNavigateUp();
    }
}