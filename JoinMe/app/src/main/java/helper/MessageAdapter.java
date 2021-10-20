package helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comp90018.JoinMe.ImageShowing;
import com.comp90018.JoinMe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import object.Message;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageArrayList;

    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.senderchatlayout, parent, false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiverchatlayout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = messageArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            if(message.getType().equalsIgnoreCase("String")){
                viewHolder.textViewmessage.setVisibility(View.VISIBLE);
                viewHolder.imageViewpic.setVisibility(View.GONE);
                viewHolder.textViewmessage.setText(message.getMessage());
                viewHolder.timeofmessage.setText(message.getCurrenttime());
            }else{
                viewHolder.textViewmessage.setVisibility(View.GONE);
                viewHolder.imageViewpic.setVisibility(View.VISIBLE);
                String uri = message.getMessage();
                Picasso.get().load(uri).into(viewHolder.imageViewpic);
                viewHolder.timeofmessage.setText(message.getCurrenttime());
                viewHolder.imageViewpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context.getApplicationContext(), "image clicked", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(v.getContext(), ImageShowing.class);
                        intent.putExtra("image", uri);
                        v.getContext().startActivity(intent);
                    }
                });

            }

        }else{
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            if(message.getType().equalsIgnoreCase("String")) {
                viewHolder.textViewmessage.setVisibility(View.VISIBLE);
                viewHolder.imageViewpic.setVisibility(View.GONE);
                viewHolder.textViewmessage.setText(message.getMessage());
                viewHolder.timeofmessage.setText(message.getCurrenttime());
            }else{
                viewHolder.textViewmessage.setVisibility(View.GONE);
                viewHolder.imageViewpic.setVisibility(View.VISIBLE);
                String uri = message.getMessage();
                Picasso.get().load(uri).into(viewHolder.imageViewpic);
                viewHolder.timeofmessage.setText(message.getCurrenttime());
                viewHolder.imageViewpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(v.getContext(), ImageShowing.class);
                        intent.putExtra("image", uri);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderID())){
            return ITEM_SEND;
        }else{
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        TextView timeofmessage;
        ImageView imageViewpic;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
            imageViewpic=itemView.findViewById(R.id.senderpic);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        TextView timeofmessage;
        ImageView imageViewpic;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
            imageViewpic=itemView.findViewById(R.id.senderpic);
        }
    }




}
