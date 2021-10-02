package com.uralsiberianworks.neuralpushkin.chatsRoom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.MainActivity;
import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.database.Chat;
import com.uralsiberianworks.neuralpushkin.database.ChatDao;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.ContactDao;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.conversationRoom.Conversation;

import java.util.List;

public class FragmentChats extends Fragment implements ChatsAdapter.ChatViewHolder.ClickListener {
    private static final String CHAT_ID = "chat_id";
    private ChatsAdapter mAdapter;
    private static boolean initialBotCreated = false;
    private ChatDao chatDao;
    private ContactDao contactDao;


    public FragmentChats(){ }


    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, null, false);
        NeuralDatabase db = ((NeuralApp) getContext().getApplicationContext()).getDb();
        chatDao = db.getChatDao();
        contactDao = db.getContactDao();
        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Messages");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatsAdapter(this, setData());
        recyclerView.setAdapter (mAdapter);

        return view;
    }


//if(!contactDao.getAllContacts().getValue().contains(new Contact("push","Alexander Pushkin",Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.push4).toString())))
    public List<Chat> setData() {
        if (initialBotCreated) {
            chatDao.getAllChats();
            mAdapter.updateLastMessage(chatDao.getAllChats());
        } else if (!chatDao.checkPushkinExist("push")) {
            Contact pushkinContact = new Contact();
            //String pushkinID = UUID.randomUUID().toString();
            String s = "push";
            pushkinContact.setContactID(s);
            String imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.push6).toString();
            pushkinContact.setImagePath(imageUri);
            pushkinContact.setName("Александр Пушкин");
            contactDao.insert(pushkinContact);

            Chat pushkinChat = new Chat();
            pushkinChat.setChatID(pushkinContact.getContactID());
            pushkinChat.setImagePath(pushkinContact.getImagePath());
            pushkinChat.setLastMessage(pushkinContact.getName() + ": Hi dear fan");
            pushkinChat.setSender(pushkinContact.getName());

            chatDao.insert(pushkinChat);
            mAdapter.updateLastMessage(chatDao.getAllChats());
            initialBotCreated = true;
        }
        return chatDao.getAllChats();
    }

    @Override
    public void onItemClicked (int position) {
        Intent intent = new Intent(getActivity(), Conversation.class);
        String id = chatDao.getAllChats().get(position).getChatID();
        intent.putExtra(CHAT_ID, id);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked (int position) {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.updateLastMessage(chatDao.getAllChats());
    }
}