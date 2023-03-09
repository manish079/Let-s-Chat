package com.project.manishprajapat.letsmessage.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.adapter.MessageAdapter;
import com.project.manishprajapat.letsmessage.model.MessageModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class ChatProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton back_btn;
    ImageView msgRead ;
    EditText itemInput;
    ImageView send_or_mic_btn, itemCameraIcon, attachmnetIcon, emojiIcon;
    CardView item_input_card_parent;
    TextView toolbarUserName;
    de.hdodenhof.circleimageview.CircleImageView toolbarImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private String senderRoom, receiverRoom;   // for getting messages
    private ArrayList<MessageModel> messageModels;
    private MessageAdapter messageAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);


//        msgRead = findViewById(R.id.msgRead);
        back_btn = findViewById(R.id.back_btn);



        toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        itemInput = findViewById(R.id.item_input);
        send_or_mic_btn = findViewById(R.id.send_mic_btn);
        item_input_card_parent = findViewById(R.id.item_input_card_parent);
        itemCameraIcon = findViewById(R.id.item_camera_icon);
        attachmnetIcon = findViewById(R.id.item_attachment_icon);
        emojiIcon = findViewById(R.id.item_emoji_icon);
        toolbarUserName = findViewById(R.id.toolbar_user);
        toolbarImage = findViewById(R.id.toolbar_image);
        recyclerView = findViewById(R.id.msg_recyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });


        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("userProfile");

        toolbarUserName.setText(userName);
        Glide.with(getApplicationContext())
                .load(profilePic)
                .placeholder(R.drawable.avtar)
                .into(toolbarImage);


        messageModels = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(), messageModels);
        recyclerView.setAdapter(messageAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);




        //creating Rooms
        String senderUId = FirebaseAuth.getInstance().getUid();
        String receiverUId = getIntent().getStringExtra("userId");
//        Toast.makeText(this, ""+receiverUId, Toast.LENGTH_SHORT).show();

        senderRoom = senderUId + receiverUId;
        receiverRoom = receiverUId + senderUId;


        reference.child("Chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    MessageModel messageModel = snapshot1.getValue(MessageModel.class);
                    messageModels.add(messageModel);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        itemInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                send_or_mic_btn.setImageResource(R.drawable.ic_baseline_send_24);
                if (itemInput.getText().toString().trim().equals("")) {
                   send_or_mic_btn.setImageResource(R.drawable.ic_baseline_mic_24);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        send_or_mic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemInput.getText().toString().trim().equals("")) {
                    Toast.makeText(ChatProfileActivity.this, "Voice your message", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(ChatProfileActivity.this, "Sending your message", Toast.LENGTH_SHORT).show();

                    //add message in firebase node
                    String messageText = itemInput.getText().toString().trim();
                    Date date = new Date();


                    MessageModel messageModel = new MessageModel(messageText, senderUId, date.getTime());
                    itemInput.setText("");

                    //push() generate a unique key every time a new child is added to the specified firebase reference.


                    HashMap<String, Object> lastMsgObj = new HashMap<>();
                    lastMsgObj.put("lastMsg", messageModel.getMessage());
                    lastMsgObj.put("lastMsgTime", messageModel.getTimeStamp());

                    firebaseDatabase.getReference().child("Chats").child(senderRoom).updateChildren(lastMsgObj);
                    firebaseDatabase.getReference().child("Chats").child(receiverRoom).updateChildren(lastMsgObj);


                    reference.child("Chats").child(senderRoom).child("messages").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
//                            Toast.makeText(getApplicationContext(), "Message added in firebase", Toast.LENGTH_SHORT).show();
                            //also message goes in receiver room now set to on layout

//                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sent_msg);
//                            mediaPlayer.start();

                            reference.child("Chats").child(receiverRoom).child("messages").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
//                                    Toast.makeText(ChatProfileActivity.this, "added in receiver room!!", Toast.LENGTH_SHORT).show();

                                    //If message successfully sent then play beep sound to receiver
//                                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.recieved_sound);
//                                    mediaPlayer.start();
                                }

                            });

                        }

                    });



                }
            }
        });
        itemCameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatProfileActivity.this, "Open Camera", Toast.LENGTH_SHORT).show();
            }
        });

        attachmnetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatProfileActivity.this, "Send images", Toast.LENGTH_SHORT).show();
            }
        });
        emojiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatProfileActivity.this, "Send emoji", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.clearChat){
            Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}