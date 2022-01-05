package com.uralsiberianworks.neuralpushkin.contactsRoom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.SetContactActivity;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;

import java.io.File;
import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> mArrayList;
    private final Context mContext;
    private static final String CONTACT_DEL = "contact_del";
    private static final String CONTACT_EDIT = "contact_edit";
    private NeuralDatabase db;


    public ContactAdapter(Context context) {
        this.mContext = context;
        db = ((NeuralApp) context.getApplicationContext()).getDb();
        this.mArrayList = db.getContactDao().getAllContacts();
        notifyDataSetChanged();
    }

    public void update() {
        mArrayList.clear();
        mArrayList = db.getContactDao().getAllContacts();
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
        /*String recipientImagePath = mArrayList.get(position).getImagePath();
        if (!mArrayList.get(position).getContactID().equals("push")) {
            File imgFile = new File(recipientImagePath);

            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Log.i("TAG", "saveImage: " + imgFile.length());
                viewHolder.userPhoto.setImageBitmap(myBitmap);
                imgFile = null;
                myBitmap = null;
                recipientImagePath = null;
            }
        }*/

        String id = mArrayList.get(position).getContactID();

        viewHolder.delBtn.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SetContactActivity.class);
            intent.putExtra(CONTACT_DEL, id);
            if (id.equals("push")) { Toast.makeText(mContext,"Оставьте Пушкина в покое",Toast.LENGTH_SHORT).show();}
            else {
                new AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            //mArrayList.remove(position);
                            db.getContactDao().del(id);
                            //mContext.startActivity(intent);
                            update();
                        })
                        .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                        }).show();
            }
        });

        viewHolder.editBtn.setOnClickListener(view -> {
            if (id.equals("push")){
                Toast.makeText(mContext,"Оставьте Пушкина в покое",Toast.LENGTH_SHORT).show(); }
            else {
            Intent intent1 = new Intent(mContext, SetContactActivity.class);
            intent1.putExtra(CONTACT_EDIT, id);
            mContext.startActivity(intent1);
            update();
        }
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
