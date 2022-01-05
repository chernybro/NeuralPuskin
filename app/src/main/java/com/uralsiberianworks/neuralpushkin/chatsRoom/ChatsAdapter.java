package com.uralsiberianworks.neuralpushkin.chatsRoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.database.Chat;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;

import java.io.File;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private List<Chat> mArrayList;
    private final ChatViewHolder.ClickListener clickListener;
    private NeuralDatabase db;
    private static boolean initialBotCreated = false;



    public ChatsAdapter(Context context, ChatViewHolder.ClickListener clickListener) {
        this.clickListener = clickListener;
        db = ((NeuralApp) context.getApplicationContext()).getDb();
        this.mArrayList = db.getChatDao().getAllChats();
        notifyDataSetChanged();
        //createPushkin();
    }

    private void createPushkin() {
        if (initialBotCreated) {
            updateLastMessage();
        } else if (!db.getChatDao().checkPushkinExist("push")) {
            Contact pushkinContact = new Contact();
            //String pushkinID = UUID.randomUUID().toString();
            String s = "push";
            pushkinContact.setContactID(s);
            String imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.push6).toString();
            pushkinContact.setImagePath(imageUri);
            pushkinContact.setName("Александр Пушкин");
            db.getContactDao().insert(pushkinContact);

            Chat pushkinChat = new Chat();
            pushkinChat.setChatID(pushkinContact.getContactID());
            pushkinChat.setImagePath(pushkinContact.getImagePath());
            pushkinChat.setLastMessage(pushkinContact.getName() + ": Hi dear fan");
            pushkinChat.setSender(pushkinContact.getName());

            db.getChatDao().insert(pushkinChat);
            updateLastMessage();
            initialBotCreated = true;
        }
    }

    public void updateLastMessage() {
        mArrayList.clear();
        mArrayList = db.getChatDao().getAllChats();
        notifyDataSetChanged();
    }



    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_chat, null);

        return new ChatViewHolder(itemLayoutView,clickListener);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(mArrayList.get(position).getSender());

        /*String recipientImagePath = mArrayList.get(position).getImagePath();

        if (!mArrayList.get(position).getChatID().equals("push")) {
             File imgFile = new File(recipientImagePath);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Log.i("TAGChatsAdapter", "saveImage: " + imgFile.length());
                viewHolder.userPhoto.setImageBitmap(myBitmap);
                myBitmap = null;
            }
            imgFile = null;

        }*/

        viewHolder.tvLastChat.setText(mArrayList.get(position).getLastMessage());

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {

        public final TextView tvName;
        public final TextView tvLastChat;
        public final ImageView userPhoto;
        private final ClickListener listener;


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
        }
    }
}
