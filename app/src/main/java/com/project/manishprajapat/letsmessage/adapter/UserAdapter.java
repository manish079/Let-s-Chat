package com.project.manishprajapat.letsmessage.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.activities.ChatProfileActivity;
import com.project.manishprajapat.letsmessage.model.User;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<User> userModelArrayList;

    public UserAdapter(Context context, ArrayList<User> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friends_profiles_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User model = userModelArrayList.get(position);


        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + model.getUserId();

        //last message aa rha he firebase se
        FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String lastMessage = snapshot.child("lastMsg").getValue(String.class);
                    try{
                        long  lastMsgTime = snapshot.child("lastMsgTime").getValue(Long.class);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        holder.lastMsgTime.setText(dateFormat.format(new Date(lastMsgTime)));



                    }
                    catch (Exception e){
                        Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    holder.lastMsg.setText(lastMessage);
                }
                else{
                    holder.lastMsg.setText("Hey! Send your first message");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        holder.imageView.setImageResource(model.getProfilePic());  //bcoz image set by glide library
        holder.userName.setText(model.getName());
//        holder.lastMsg.setText(model.getIncomingMsg());
//        holder.latestMsg.setText(model.getTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new  Intent(context.getApplicationContext(), ChatProfileActivity.class);
            intent.putExtra("userName", model.getName());
            intent.putExtra("userProfile", model.getProfilePic());
            intent.putExtra("userId", model.getUserId());

            context.startActivity(intent);
            }
        });

                Glide
                .with(context)
                .load(model.getProfilePic())
                .placeholder(R.drawable.avtar)
                .into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "View profile pic", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView userName, lastMsgTime, lastMsg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            imageView = itemView.findViewById(R.id.user_img);
            lastMsgTime = itemView.findViewById(R.id.current_msg_time);
            lastMsg = itemView.findViewById(R.id.lastMsg);

        }
    }
}
