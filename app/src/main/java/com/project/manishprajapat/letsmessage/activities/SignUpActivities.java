package com.project.manishprajapat.letsmessage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.databinding.ActivitySignUpActivitiesBinding;
import com.project.manishprajapat.letsmessage.model.User;

import java.util.Objects;
import java.util.UUID;

public class SignUpActivities extends AppCompatActivity {
    //    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 123;
    private final String TAG = "signup";
    ActivitySignUpActivitiesBinding signUpActivitiesBinding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    FirebaseStorage firebaseStorage;
    ConstraintLayout constraintLayout;
    ProgressDialog progressDialog;
    FirebaseUser currentUser;
    Uri selectedImagePath = null;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpActivitiesBinding = ActivitySignUpActivitiesBinding.inflate(getLayoutInflater());
        setContentView(signUpActivitiesBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account...");

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Open Gallary Images
        signUpActivitiesBinding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Gallary
                openGallary();
            }
        });

        signUpActivitiesBinding.buttonSuSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserLoginCredietial();
            }
        });

        signUpActivitiesBinding.tvSuAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpActivitiesBinding.tvSuGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Google SignIn...");
                progressDialog.show();
                signIn();
            }
        });
    }

    public void openGallary() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, 200);

        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Gallery image set on imageView
        if (data != null) {
            if (data.getData() != null) {
                selectedImagePath = data.getData(); //address of image
                signUpActivitiesBinding.imageView.setImageURI(selectedImagePath);
            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                createUserAccountWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void createUserAccountWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            currentUser = firebaseAuth.getCurrentUser();

                            //Store google signIn data in firebase database
                            User users = new User();
                            assert currentUser != null;
                            users.setUserId(currentUser.getUid());
                            users.setName(currentUser.getDisplayName());
                            users.setEmail(currentUser.getEmail());
                            users.setProfilePic(currentUser.getPhotoUrl().toString());

                            databaseReference = firebaseDatabase.getReference("Users").child(currentUser.getUid());
                            databaseReference.setValue(users);

                            startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

    private void getUserLoginCredietial() {

        String userName = signUpActivitiesBinding.etSuName.getText().toString().trim();
        String email = signUpActivitiesBinding.etSuEmail.getText().toString().trim();
        String password = signUpActivitiesBinding.etSuPassword.getText().toString().trim();
        String confirmPass = signUpActivitiesBinding.etSuConfPassword.getText().toString().trim();

        //Profile image upload on firebase storage
        if (selectedImagePath != null) {
            StorageReference storageReference = firebaseStorage.getReference("ProfilePictures/" + UUID.randomUUID().toString());
            storageReference.putFile(selectedImagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //get image url in string format
                                String imageUrl = uri.toString();

                                if (!userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPass.isEmpty()) {
                                    if (password.equals(confirmPass) && email.matches("\\b[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}\\b")) {
                                        //create account of user
                                        progressDialog.show();
                                        //new user auth using email
                                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                progressDialog.dismiss();

                                                if (task.isSuccessful()) {
//                                                    after user register using email now store data of user according to his uId which created at signup time

                                                    String userId = task.getResult().getUser().getUid();

//                                                    User user = new User(userName, email, password, imageUrl);
                                                    User user = new User(userId, userName, email, password, imageUrl);

                                                    databaseReference = firebaseDatabase.getReference("Users").child(userId);
                                                    databaseReference.setValue(user);

                                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                    finish();

                                                    Log.d(TAG, "createUserWithEmail: " + "Successfull user created");

                                                }
                                                else {
                                                    Log.d(TAG, "createUserWithEmail: " + task.getException().getMessage());
                                                    Toast.makeText(SignUpActivities.this, "No User created", Toast.LENGTH_SHORT).show();

                                                    //after user creted detail deleted from edit text;
                                                    signUpActivitiesBinding.etSuName.setText("");
                                                    signUpActivitiesBinding.etSuEmail.setText("");
                                                    signUpActivitiesBinding.etSuPassword.setText("");
                                                    signUpActivitiesBinding.etSuConfPassword.setText("");
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid password match", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "fill all detail", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        Toast.makeText(SignUpActivities.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivities.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
        super.onBackPressed();
    }


}