package com.project.manishprajapat.letsmessage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.fragments.ChatFragment;
import com.project.manishprajapat.letsmessage.fragments.ProfileFragment;
import com.project.manishprajapat.letsmessage.fragments.StatusFragment;

public class MainDashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        searchView = findViewById(R.id.app_bar_search);


        firebaseAuth = FirebaseAuth.getInstance();

        //set default fragment -> chat fragment

        ChipNavigationBar bottomNavigationView = findViewById(R.id.menu);

        bottomNavigationView.setItemSelected(R.id.chat,true);

        ChatFragment chatFragment = new ChatFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.parentDashboard, chatFragment);
        fragmentTransaction.commit();

        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.status:
                       fragment = new StatusFragment();
                        break;
                    case R.id.user_profile:
                        fragment = new ProfileFragment();
                            break;
                    default:
                       fragment = new ChatFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.parentDashboard,
                                fragment).commit();
            }
        });



        //Main Dashboard Search View



    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_dashboard, menu);

      return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out_btn:
                signOutUser();
                Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.invite:
                Toast.makeText(this, "invite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.group:
                Toast.makeText(this, "Group", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void signOutUser() {
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }
}