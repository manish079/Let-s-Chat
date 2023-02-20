package com.project.manishprajapat.letsmessage.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.project.manishprajapat.letsmessage.R;
import com.project.manishprajapat.letsmessage.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageModelArrayList;
    final int MESSAGE_SENT = 1;  //view type of sender is 1
    final int MESSAGE_RECEIVE = 2;  //view type pf receiver is 2

    public MessageAdapter(Context context, ArrayList<MessageModel> messageModelArrayList) {
        this.context = context;
        this.messageModelArrayList = messageModelArrayList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MESSAGE_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SentViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    // Sender will be only one person that is who signIn in app
    // message receiver from other users except current signIn user
    //Sender will be only one person but receiver may be multiple -> Handling sending msg or receiving msg

    @Override
    public int getItemViewType(int position) {

        //getting signIn user only receive msg from others
        if(messageModelArrayList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return MESSAGE_SENT;
        }
        else{

            return MESSAGE_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageModel model = messageModelArrayList.get(position);

//        int reactions[] =  new int[]{
//                R.drawable.thumbup,
//                R.drawable.heart,
//                R.drawable.laugh,
//                R.drawable.wao,
//                R.drawable.sad,
//                R.drawable.angry};
//
//        ReactionsConfig config = new ReactionsConfigBuilder(context)
//                .withReactions(reactions)
//                .build();

//        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
//            if(holder.getClass() == SentViewHolder.class){
//                SentViewHolder sentViewHolder = (SentViewHolder) holder;
//
//            }
//            return true; // true is closing popup, false is requesting a new selection
//        });

        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder sentViewHolder = (SentViewHolder) holder;
            sentViewHolder.senderMsg.setText(model.getMessage());

            try{
//                long  lastMsgTime = snapshot.child("lastMsgTime").getValue(Long.class);
                  SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                  sentViewHolder.senderTimeStamp.setText(dateFormat.format(new Date()));


            }
            catch (Exception e){
                Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

//            sentViewHolder.senderTimeStamp.setText(""+model.getTimeStamp());

            //adding reaction on sender message

//            holder.itemView.findViewById(R.id.senderCardView).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                   popup.onTouch(view, motionEvent);
//                    return false;
//                }
//            });
        }
        else{



            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.receiverMsg.setText(model.getMessage());

            try{
//                long  lastMsgTime = snapshot.child("lastMsgTime").getValue(Long.class);


                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                receiverViewHolder.receiverTimeStamp.setText(dateFormat.format(new Date()));
            }
            catch (Exception e){
                Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            //adding reaction on receiving message
//            holder.itemView.findViewById(R.id.receiverCardView).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    popup.onTouch(view, motionEvent);
//                    return false;
//                }
//            });



        }

    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }


    public class SentViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg, senderTimeStamp;
        ImageView msgRead;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.text_content);
            senderTimeStamp = itemView.findViewById(R.id.senderTimeStamp);
//            msgRead = itemView.findViewById(R.id.msgRead);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg, receiverTimeStamp;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiver_text_content);
            receiverTimeStamp = itemView.findViewById(R.id.receiverTimeStamp);
        }
    }

}
