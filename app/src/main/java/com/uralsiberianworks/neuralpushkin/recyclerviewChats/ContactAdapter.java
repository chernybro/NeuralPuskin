package com.uralsiberianworks.neuralpushkin.recyclerviewChats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.uralsiberianworks.neuralpushkin.AddContactActivity;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.Contact;

import java.io.File;
import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<com.uralsiberianworks.neuralpushkin.db.Contact> mArrayList;
    private Context mContext;
    private ContactAdapter.ViewHolder.ClickListener clickListener;
    private static final String CONTACT_DEL = "contact_del";
    private static final String CONTACT_EDIT = "contact_edit";




    public ContactAdapter (Context context, List<com.uralsiberianworks.neuralpushkin.db.Contact> arrayList, ContactAdapter.ViewHolder.ClickListener clickListener) {
        this.mArrayList = arrayList;
        this.mContext = context;
        this.clickListener = clickListener;

    }

    public void update(List<Contact> arrayList) {
        this.mArrayList = arrayList;
        notifyDataSetChanged();
    }

    // Create new views
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_contact, null);

        ContactAdapter.ViewHolder viewHolder = new ContactAdapter.ViewHolder(itemLayoutView,clickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(mArrayList.get(position).getName());
        String recipientImagePath = mArrayList.get(position).getImagePath();
        File imgFile = new File(recipientImagePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            viewHolder.userPhoto.setImageBitmap(myBitmap);
        }

        viewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddContactActivity.class);
                String id = mArrayList.get(position).getContactID();
                intent.putExtra(CONTACT_DEL, id);

                new AlertDialog.Builder(mContext)
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mArrayList.remove(position);
                        mContext.startActivity(intent);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });

        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddContactActivity.class);
                String id = mArrayList.get(position).getContactID();
                intent.putExtra(CONTACT_EDIT, id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener  {

        public TextView tvName;
        public ImageView userPhoto;
        private ContactAdapter.ViewHolder.ClickListener listener;
        private ImageButton editBtn;
        private ImageButton delBtn;


        public ViewHolder(View itemLayoutView, ContactAdapter.ViewHolder.ClickListener listener) {
            super(itemLayoutView);

            this.listener = listener;

            tvName = itemLayoutView.findViewById(R.id.tv_user_name);
            userPhoto = itemLayoutView.findViewById(R.id.iv_user_photo);
            editBtn = itemLayoutView.findViewById(R.id.edit_btn);
            delBtn = itemLayoutView.findViewById(R.id.del_btn);

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
