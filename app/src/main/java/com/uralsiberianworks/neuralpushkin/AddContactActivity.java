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
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.ChatDao;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.Message;
import com.uralsiberianworks.neuralpushkin.db.MessageDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ConversationAdapter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AddContactActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static final String TAG = "AddContactActivity";
    NeuralDatabase db;
    private ContactDao contactDao;
    private ChatDao chatDao;
    private MessageDao messageDao;
    private ImageView editImage;
    private EditText etContactName;
    private Button addButton;
    private String imagePath;
    private String id = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        db = ((NeuralApp)getApplication()).getDb();
        contactDao = db.getContactDao();
        chatDao = db.getChatDao();
        messageDao = db.getMessageDao();

        editImage = findViewById(R.id.edit_contact_image);
        etContactName = findViewById(R.id.et_name_contact);
        addButton = findViewById(R.id.add_contact_btn);

        if (getIntent().hasExtra("contact_del")) {
            Bundle arguments = getIntent().getExtras();
            id = arguments.get("contact_del").toString();

            if (!id.equals("0")) {
                contactDao.del(id);
                finish();
            }
        } else if (getIntent().hasExtra("contact_edit")) {
            Bundle arguments = getIntent().getExtras();
            id = arguments.get("contact_edit").toString();
            if (!id.equals("0")) {
                Contact contact = contactDao.getContact(id);
                String path = contact.getImagePath();
                File imgFile = new File(path);

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    editImage.setImageBitmap(myBitmap);
                    imagePath = imgFile.getAbsolutePath();
                }
                etContactName.setText(contact.getName());
                imageListener();
                addContactButtonListener(contact);

            }
        } else {


            saveImage();

            setListeners();
        }
    }


    private void setListeners() {
        imageListener();
        addContactButtonListener();
    }

    private void addContactButtonListener() {
        addButton.setOnClickListener(view -> {
            if (!etContactName.getText().equals("")) {

                setNewContact();

            }
        });
    }

    private void addContactButtonListener(Contact contact) {
        addButton.setOnClickListener(view -> {
            if (!etContactName.getText().equals("")) {

                updateContact(contact);

            }
        });
    }

    private void updateContact(Contact contact) {
        contact.setName(etContactName.getText().toString());
        contact.setImagePath(imagePath);
        contactDao.update(contact);

        Chat chat = chatDao.getChatFromID(contact.getContactID());
        chat.setSender(contact.getName());
        chat.setImagePath(contact.getImagePath());
        chatDao.update(chat);

        finish();

    }

    private void setNewContact() {
        if (!TextUtils.isEmpty(etContactName.getText().toString())) {


            Contact contact = new Contact();
            contact.setName(etContactName.getText().toString());
            contact.setContactID(UUID.randomUUID().toString());
            contact.setImagePath(imagePath);
            contactDao.insert(contact);

            Chat chat = new Chat();
            chat.setLastMessage("Hi");
            chat.setSender(contact.getName());
            chat.setImagePath(contact.getImagePath());
            chat.setChatID(contact.getContactID());
            chatDao.insert(chat);

            Message message = new Message();

            message.setMessageID(String.valueOf(UUID.randomUUID()));
            message.setChatID(chat.getChatID());
            message.setInitialLength(chat.getLastMessage().length());
            message.setType("1");
            message.setText(chat.getLastMessage());
            messageDao.insert(message);

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

        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
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
    }
    private void saveImage(){
        editImage.setImageResource(R.drawable.ic_baseline_account_circle_24);
        Drawable drawable = editImage.getDrawable();

        Bitmap bitmap = takeBitmap(drawable);

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        File file = wrapper.getDir("Images", MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString()+".jpg");

        try{
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
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
