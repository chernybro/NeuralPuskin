package com.uralsiberianworks.neuralpushkin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.uralsiberianworks.neuralpushkin.recyclerviewChats.Chat;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHolder extends Fragment implements ChatAdapter.ChatViewHolder.ClickListener {
    public static final int REQUEST_CODE = 100;
    private static final String CHAT_ID = "chat_id";
    private static final String LASTMESSAGE = "lastmessage";
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;

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
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_recycler, null, false);

        getActivity().supportInvalidateOptionsMenu();

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatAdapter(getContext(),setData(),this);
        mRecyclerView.setAdapter (mAdapter);

        return view;
    }
    public List<Chat> setData(){
        List<Chat> data = new ArrayList<>();
        String[] name = {"Alexander Pushkin", "Angela Price"};
        String[] lastMessage = {"Hi dear fan", "Would you fuck with me?"};
        @DrawableRes int[] img = {R.drawable.push5 , R.drawable.userpic};

        for (int i = 0; i<2; i++){
            Chat chat = new Chat();
            chat.setTime("5:04pm");
            chat.setName(name[i]);
            chat.setImage(img[i]);
            chat.setLastChat(lastMessage[i]);
            data.add(chat);
        }
        return data;
    }

    @Override
    public void onItemClicked (int position) {
        startActivityForResult(new Intent(getActivity(), Conversation.class), REQUEST_CODE);
    }

    @Override
    public boolean onItemLongClicked (int position) {
        toggleSelection(position);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void toggleSelection(int position) { }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
