package com.uralsiberianworks.neuralpushkin.ContactsRoom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.uralsiberianworks.neuralpushkin.SetContactActivity;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Contact;

import java.io.File;
import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> mArrayList;
    private final Context mContext;
    private static final String CONTACT_DEL = "contact_del";
    private static final String CONTACT_EDIT = "contact_edit";




    public ContactAdapter (Context context) {
        this.mContext = context;

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

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tvName.setText(mArrayList.get(position).getName());
        String recipientImagePath = mArrayList.get(position).getImagePath();
        if (!recipientImagePath.equals(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.push6).toString())) {
            File imgFile = new File(recipientImagePath);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                viewHolder.userPhoto.setImageBitmap(myBitmap);
            }
        }

        viewHolder.delBtn.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SetContactActivity.class);
            String id = mArrayList.get(position).getContactID();
            intent.putExtra(CONTACT_DEL, id);

            new AlertDialog.Builder(mContext)
            .setMessage("Are you sure you want to delete this contact?")
            .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                mArrayList.remove(position);
                mContext.startActivity(intent);
                notifyDataSetChanged();
            })
            .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
            }).show();

        });

        viewHolder.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SetContactActivity.class);
            String id = mArrayList.get(position).getContactID();
            intent.putExtra(CONTACT_EDIT, id);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public final TextView tvName;
        public final ImageView userPhoto;
        private final ImageButton editBtn;
        private final ImageButton delBtn;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = itemLayoutView.findViewById(R.id.tv_user_name);
            userPhoto = itemLayoutView.findViewById(R.id.iv_user_photo);
            editBtn = itemLayoutView.findViewById(R.id.edit_btn);
            delBtn = itemLayoutView.findViewById(R.id.del_btn);
        }
    }
}
