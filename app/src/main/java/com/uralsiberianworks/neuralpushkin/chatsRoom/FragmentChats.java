package com.uralsiberianworks.neuralpushkin.chatsRoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.MainActivity;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.conversationRoom.Conversation;


public class FragmentChats extends Fragment implements ChatsAdapter.ChatViewHolder.ClickListener {
    private static final String POSITION = "position";
    private ChatsAdapter mAdapter;


    public FragmentChats(){ }


    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, null, false);
        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Messages");
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ChatsAdapter(getContext(),this);
        recyclerView.setAdapter (mAdapter);
        mAdapter.updateLastMessage();
        return view;
    }

    @Override
    public void onItemClicked (int position) {
        Intent intent = new Intent(getActivity(), Conversation.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClicked (int position) {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.updateLastMessage();
    }
}
