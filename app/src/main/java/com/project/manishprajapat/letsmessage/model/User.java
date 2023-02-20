package com.project.manishprajapat.letsmessage.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.manishprajapat.letsmessage.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String password;
    public String email;
    public String profilePic;  //if user google signUp then pic store in db
    public String userId;
    public String time;

    public User(){
        //required for calls to DataSnapshot.getValue(User.class)
    }

//    public User(String name, String email, String password, String profilePic) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.profilePic=profilePic;
//    }
    public User(String userId, String name, String email, String password, String profilePic) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePic=profilePic;
    }

//    public User(String userId){
//        this.userId = userId;
//    }
    public User(String name, String profile){
        this.name = name;
        this.profilePic = profile;
    }



//    public void WriteUserInFirebase(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Map<String, User> userMap = new HashMap<>();
//        userMap.put(name, new User(name, email, password, profilePic));
////        userMap.put(name, new User(profilePic));
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(String email) {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public  static ArrayList<User> getUserProfiles(){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Manish",String.valueOf(R.drawable.manish)));
        users.add(new User("Kamal",String.valueOf(R.drawable.manish)));
        users.add(new User("Raju", String.valueOf(R.drawable.manish)));
        users.add(new User("Manish", String.valueOf(R.drawable.manish)));
        users.add(new User("Manish",String.valueOf(R.drawable.manish)));

        return users;
    }
}
