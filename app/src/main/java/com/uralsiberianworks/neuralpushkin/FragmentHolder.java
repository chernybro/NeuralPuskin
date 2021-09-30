package com.uralsiberianworks.neuralpushkin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.ChatDao;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerConversation.ConversationAdapter;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.ChatsAdapter;

import java.util.List;

public class FragmentHolder extends Fragment implements ChatsAdapter.ChatViewHolder.ClickListener {
    private static final String CHAT_ID = "chat_id";
    private ChatsAdapter mAdapter;
    private ChatDao chatDao;
    private ContactDao contactDao;
    private static boolean initialBotCreated = false;
    private List<Chat> dataList;
    int clickedPosition;


    public void onCreate(Bundle a){
        super.onCreate(a);
        NeuralDatabase db = ((NeuralApp) getActivity().getApplication()).getDb();
        contactDao = db.getContactDao();
        chatDao = db.getChatDao();

        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_recycler, null, false);

        getActivity().invalidateOptionsMenu();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatsAdapter(getContext(),setData(),this);

        recyclerView.setAdapter (mAdapter);

        return view;
    }
    public List<Chat> setData() {
        if (initialBotCreated) {
            return dataList = chatDao.getAllChats();
        }
        else if(!contactDao.getAllContacts().contains(new Contact("push","Alexander Pushkin",Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.push4).toString()))){
            Contact pushkinContact = new Contact();
            //String pushkinID = UUID.randomUUID().toString();
            String s = "push";
            pushkinContact.setContactID(s);
            String imageUri = Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" + R.drawable.push4).toString();
            pushkinContact.setImagePath(imageUri);
            pushkinContact.setName("Alexander Pushkin");
            contactDao.insert(pushkinContact);

            Chat pushkinChat = new Chat();
            pushkinChat.setChatID(pushkinContact.getContactID());
            pushkinChat.setImagePath(pushkinContact.getImagePath());
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
        dataList = chatDao.getAllChats();
        String id = dataList.get(position).getChatID();
        intent.putExtra(CHAT_ID, id);
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
