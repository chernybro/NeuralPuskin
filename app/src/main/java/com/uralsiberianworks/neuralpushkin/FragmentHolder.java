package com.uralsiberianworks.neuralpushkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;


import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.ChatDao;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ChatData;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.ChatAdapter;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FragmentHolder extends Fragment implements ChatAdapter.ChatViewHolder.ClickListener {
    public static final int REQUEST_CODE = 100;
    private static final String CHAT_ID = "chat_id";
    private static final String POSITION = "position";
    private static final String LASTMESSAGE = "lastmessage";
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    ChatDao chatDao;
    ContactDao contactDao;
    NeuralDatabase db;
    private static boolean initialBotCreated = false;
    private List<Chat> dataList;
    int clickedPosition;

    public static FragmentHolder newInstance(int chatID, String lastMessage){
        FragmentHolder fragment = new FragmentHolder();
        Bundle bundle = new Bundle();
        bundle.putInt(CHAT_ID, chatID);
        bundle.putString(LASTMESSAGE, lastMessage);
        fragment.setArguments(bundle);
        return fragment;

    }
    public void onCreate(Bundle a){
        super.onCreate(a);
        db = ((NeuralApp)getActivity().getApplication()).getDb();
        contactDao = db.getContactDao();
        chatDao = db.getChatDao();

        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_recycler, null, false);

        getActivity().invalidateOptionsMenu();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatAdapter(getContext(),setData(),this);
        mRecyclerView.setAdapter (mAdapter);

        return view;
    }
    public List<Chat> setData() {
        if (initialBotCreated) {
            return dataList = chatDao.getAllChats();
        }
        else if(!contactDao.getAllContacts().contains(new Contact("push","Alexander Pushkin",R.drawable.push5))){
            Contact pushkinContact = new Contact();
            //String pushkinID = UUID.randomUUID().toString();
            String s = "push";
            pushkinContact.setContactID(s);
            pushkinContact.setImage(R.drawable.push5);
            pushkinContact.setName("Alexander Pushkin");
            contactDao.insert(pushkinContact);

            Chat pushkinChat = new Chat();
            pushkinChat.setChatID(pushkinContact.getContactID());
            pushkinChat.setImage(pushkinContact.getImage());
            pushkinChat.setLastMessage("Hi dear fan");
            pushkinChat.setSender(pushkinContact.getName());

            chatDao.insert(pushkinChat);
            initialBotCreated = true;
            return dataList = chatDao.getAllChats();
        }
        return dataList = chatDao.getAllChats();
    }

    @Override
    public void onItemClicked (int position) {
        clickedPosition = position;
        Intent intent = new Intent(getActivity(), Conversation.class);
        String id = dataList.get(position).getChatID();
        intent.putExtra(CHAT_ID, id);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked (int position) {
        toggleSelection(position);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.updateLastMessage(chatDao.getAllChats());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void toggleSelection(int position) { }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { }
}
