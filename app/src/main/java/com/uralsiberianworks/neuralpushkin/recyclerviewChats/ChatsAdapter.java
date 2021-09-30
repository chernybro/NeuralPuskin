package com.uralsiberianworks.neuralpushkin.recyclerviewChats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.AddContactActivity;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Chat;

import java.io.File;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private List<com.uralsiberianworks.neuralpushkin.db.Chat> mArrayList;
    private Context mContext;
    private ChatViewHolder.ClickListener clickListener;



    public ChatsAdapter(Context context, List<Chat> arrayList, ChatViewHolder.ClickListener clickListener) {
        this.mArrayList = arrayList;
        this.mContext = context;
        this.clickListener = clickListener;

    }

    public void updateLastMessage(List<Chat> arrayList) {
        this.mArrayList = arrayList;
        notifyDataSetChanged();
    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_chat, null);

        ChatViewHolder viewHolder = new ChatViewHolder(itemLayoutView,clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(mArrayList.get(position).getSender());
        String recipientImagePath = mArrayList.get(position).getImagePath();
        File imgFile = new File(recipientImagePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            viewHolder.userPhoto.setImageBitmap(myBitmap);
        }
        viewHolder.tvLastChat.setText(mArrayList.get(position).getLastMessage());



    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {

        public TextView tvName;
        public TextView tvLastChat;
        public ImageView userPhoto;
        private ClickListener listener;

        //private final View selectedOverlay;


        public ChatViewHolder(View itemLayoutView,ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_user_name);
            tvLastChat = (TextView) itemLayoutView.findViewById(R.id.tv_last_chat);
            userPhoto = (ImageView) itemLayoutView.findViewById(R.id.rl_photo);



            itemLayoutView.setOnClickListener(this);

            itemLayoutView.setOnLongClickListener (this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition ());
            }

        }
        @Override
        public boolean onLongClick (View view) {
            if (listener != null) {
                return listener.onItemLongClicked(getAdapterPosition ());
            }
            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);

            boolean onCreateOptionsMenu(Menu menu);
        }
    }
}
