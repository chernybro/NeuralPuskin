package com.uralsiberianworks.neuralpushkin;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uralsiberianworks.neuralpushkin.chatsRoom.ChatsAdapter;
import com.uralsiberianworks.neuralpushkin.contactsRoom.ContactAdapter;
import com.uralsiberianworks.neuralpushkin.database.Chat;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.Message;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class SetContactActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static final String TAG = "AddContactActivity";
    private ImageView editImage;
    private EditText etContactName;
    private Button addButton;
    private String imagePath;
    private NeuralDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = ((NeuralApp) getApplication()).getDb();

        editImage = findViewById(R.id.edit_contact_image);
        etContactName = findViewById(R.id.et_name_contact);
        addButton = findViewById(R.id.add_contact_btn);

        String id;
        if (getIntent().hasExtra("contact_edit")) {
            Bundle arguments = getIntent().getExtras();
            id = arguments.get("contact_edit").toString();
            if (!id.equals("0")) {
                Contact contact = db.getContactDao().getContact(id);
                String path = contact.getImagePath();
                if (!path.equals(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.push6).toString())) {
                    File imgFile = new File(path);

                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        editImage.setImageBitmap(myBitmap);
                        imagePath = imgFile.getAbsolutePath();
                        Log.i(TAG, "saveImage: " + imgFile.length());
                        imgFile = null;
                        myBitmap = null;
                    }
                } else saveImage();
                etContactName.setText(contact.getName());
                imageListener();
                addContactButtonListener(contact);
            }
        } else {

            saveImage();

            setListeners();
        }
        //new ChatsAdapter(getApplicationContext(),null);
        //new ContactAdapter(getApplicationContext());
    }


    private void setListeners() {
        imageListener();
        addContactButtonListener();
    }

    private void addContactButtonListener() { addButton.setOnClickListener(view -> { if (!etContactName.getText().equals("")) { setNewContact(); } }); }

    private void addContactButtonListener(Contact contact) {
        addButton.setOnClickListener(view -> { if (!etContactName.getText().equals("")) { updateContact(contact); } });
    }

    private void updateContact(Contact contact) {
        int lastNameLength = contact.getName().length();
        contact.setName(etContactName.getText().toString());
        contact.setImagePath(imagePath);
        db.getContactDao().update(contact);

        Chat chat = db.getChatDao().getChatFromID(contact.getContactID());
        chat.setSender(contact.getName());
        chat.setImagePath(contact.getImagePath());
        chat.setLastMessage(contact.getName() + chat.getLastMessage().substring(lastNameLength));
        db.getChatDao().update(chat);
        db = null;
        finish();

    }

    private void setNewContact() {
        if (!TextUtils.isEmpty(etContactName.getText().toString())) {


            Contact contact = new Contact();
            contact.setName(etContactName.getText().toString());
            contact.setContactID(UUID.randomUUID().toString());
            contact.setImagePath(imagePath);
            db.getContactDao().insert(contact);

            Chat chat = new Chat();
            chat.setLastMessage(contact.getName() + ": Hi");
            chat.setSender(contact.getName());
            chat.setImagePath(contact.getImagePath());
            chat.setChatID(contact.getContactID());
            db.getChatDao().insert(chat);

            Message message = new Message();

            message.setMessageID(String.valueOf(UUID.randomUUID()));
            message.setChatID(chat.getChatID());
            message.setInitialLength(0);
            message.setType("1");
            message.setText(chat.getLastMessage().substring(2+chat.getSender().length()));
            db.getMessageDao().insert(message);
            db = null;
            finish();
        }
    }



    private void imageListener() {
        editImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            saveImage(data);
            Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(Intent data){
        editImage.setImageURI(data.getData());
        Drawable drawable = editImage.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        File file = wrapper.getDir("Images", MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString()+".jpg");
        Log.i(TAG, "saveImage1: " + file.length());
        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,5,stream);
            Log.i(TAG, "saveImage2: " + file.length());
            stream.flush();
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Display the saved image to ImageView
        editImage.setImageURI(savedImageURI);
        imagePath = file.getAbsolutePath();
        file = null;
        bitmap = null;
        wrapper = null;
        drawable = null;
    }
    private void saveImage(){
        //editImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
        Drawable drawable = editImage.getDrawable();

        Bitmap bitmap = takeBitmap(drawable);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        File file = wrapper.getDir("Images", MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString()+".jpg");

        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            Log.i(TAG, "saveImage: " + file.length());
            stream.flush();
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Display the saved image to ImageView
        editImage.setImageURI(savedImageURI);
        imagePath = file.getAbsolutePath();
        file = null;
        bitmap = null;
        wrapper = null;
        drawable = null;
    }

    private Bitmap takeBitmap(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }
}
