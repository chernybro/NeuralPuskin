package com.uralsiberianworks.neuralpushkin.recyclerConversation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Message;

import java.io.File;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private List<Message> items;
    final String recipientImagePath;

    private final int YOU = 1, ME = 2, TYPING = 3;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ConversationAdapter(List<Message> items,String recipientImagePath) {
        this.items = items;
        this.recipientImagePath = recipientImagePath;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateLastMessage(List<Message> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        switch (items.get(position).getType()) {
            case "1":
                return YOU;
            case "2":
                return ME;
            case "3":
                return TYPING;
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

                File imgFile = new File(recipientImagePath);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    imageView.setImageBitmap(myBitmap);
                }
                viewHolder = new HolderMessageView(v2);
                break;
            case ME:
                View v = inflater.inflate(R.layout.layout_holder_me, viewGroup, false);
                viewHolder = new HolderMessageView(v);
                break;
            case TYPING:
                View v3 = inflater.inflate(R.layout.layout_holder_typing, viewGroup, false);
                viewHolder = new HolderMessageView(v3);
                break;
            default:
                View v4 = inflater.inflate(R.layout.layout_holder_typing, viewGroup, false);
                viewHolder = new HolderMessageView(v4);
                break;
        }
        return viewHolder;
    }
    public void addItem(List<Message> item) {
        items.addAll(item);
        notifyDataSetChanged();
    }

    public void delItem(Message message) {
        items.remove(message);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case YOU:
                HolderMessageView vh2 = (HolderMessageView) viewHolder;
                configureViewHolderYou(vh2, position);
                break;
            case ME:
                HolderMessageView vh = (HolderMessageView) viewHolder;
                configureViewHolderMe(vh, position);
                break;
            default:
                HolderMessageView vh3 = (HolderMessageView) viewHolder;
                configureViewHolderTyping(vh3, position);
                break;
        }
    }

    private void configureViewHolderMe(HolderMessageView vh1, int position) {
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolderYou(HolderMessageView vh1, int position) {
        final SpannableStringBuilder sb = new SpannableStringBuilder(items.get(position).getText());
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(158, 158, 158));
        int colorLength = items.get(position).getInitialLength();
        sb.setSpan(fcs, 0, colorLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        vh1.getChatText().setText(sb);
    }

    private void configureViewHolderTyping(HolderMessageView vh1, int position) {
        vh1.getChatText().setText(items.get(position).getText());
    }
}
