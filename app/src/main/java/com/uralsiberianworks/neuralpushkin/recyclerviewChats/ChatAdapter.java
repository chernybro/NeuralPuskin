package com.uralsiberianworks.neuralpushkin.recyclerviewChats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> mArrayList;
    private Context mContext;
    private ChatViewHolder.ClickListener clickListener;



    public ChatAdapter (Context context, List<Chat> arrayList, ChatViewHolder.ClickListener clickListener) {
        this.mArrayList = arrayList;
        this.mContext = context;
        this.clickListener = clickListener;

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

        viewHolder.tvName.setText(mArrayList.get(position).getName());
        viewHolder.userPhoto.setImageResource(mArrayList.get(position).getImage());

        viewHolder.tvLastChat.setText(mArrayList.get(position).getLastChat());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener  {

        public TextView tvName;
        public TextView tvTime;
        public TextView tvLastChat;
        public ImageView userPhoto;
        public boolean online = false;
        private ClickListener listener;
        //private final View selectedOverlay;


        public ChatViewHolder(View itemLayoutView,ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = (TextView) itemLayoutView.findViewById(R.id.tv_user_name);
            //selectedOverlay = (View) itemView.findViewById(R.id.selected_overlay);
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
            public void onItemClicked(int position);

            public boolean onItemLongClicked(int position);

            boolean onCreateOptionsMenu(Menu menu);
        }
    }
}
