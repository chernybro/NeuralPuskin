package com.uralsiberianworks.neuralpushkin.recyclerConversation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Message;

import java.util.List;

public class ConversationRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Message> items;
    int recipientImage;

    private final int YOU = 1, ME = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ConversationRecyclerView(List<Message> items, int recipientImage) {
        this.items = items;
        this.recipientImage = recipientImage;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position).getType().equals("1")) {
            return YOU;
        }else if (items.get(position).getType().equals("2")) {
            return ME;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case YOU:
                View v2 = inflater.inflate(R.layout.layout_holder_you, viewGroup, false);
                ImageView imageView = v2.findViewById(R.id.recipient_img);
                imageView.setImageResource(recipientImage);
                viewHolder = new HolderYou(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;
    }
    public void addItem(List<Message> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case YOU:
                HolderYou vh2 = (HolderYou) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            default:
                HolderMe vh = (HolderMe) viewHolder;
                configureViewHolder3(vh, position);
                break;
        }
    }

    private void configureViewHolder3(HolderMe vh1, int position) {
        //vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolder2(HolderYou vh1, int position) {
        //vh1.getTime().setText(items.get(position).getTime());
        vh1.getChatText().setText(items.get(position).getText());
    }
}
