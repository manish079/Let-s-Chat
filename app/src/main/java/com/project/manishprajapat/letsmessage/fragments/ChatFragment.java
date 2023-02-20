package com.project.manishprajapat.letsmessage.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.adapter.UserAdapter;
import com.project.manishprajapat.letsmessage.model.User;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    MenuItem searchViewItem;
    SearchView searchView;
    UserAdapter userAdapter;
    RecyclerView recyclerView;

    ArrayList<User> usersModel = User.getUserProfiles() ;
    ArrayList<User> users;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = new ArrayList<>();

        loadUserFromFirebase();  //load user on chat activity from firebase

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

       userAdapter = new UserAdapter(getContext(), users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAdapter);



        return view;
    }

    private void loadUserFromFirebase() {

        FirebaseUser currentUser;
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              users.clear();

              for(DataSnapshot snapshot1 : snapshot.getChildren()){
                 User user = snapshot1.getValue(User.class);
                  assert user != null;
                  user.setUserId(snapshot1.getKey());
                    users.add(user);
              }
              userAdapter.notifyDataSetChanged();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
          }
      });
    }


}

