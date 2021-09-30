package com.uralsiberianworks.neuralpushkin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.ChatDao;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.ContactAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentContacts extends Fragment {

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private static boolean initialBotCreated = false;
    private List<Contact> dataList;
    private NeuralDatabase db;
    private ContactDao contactDao;
    private ChatDao chatDao;


    public FragmentContacts(){
        setHasOptionsMenu(true);
    }
    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
        db = ((NeuralApp) getActivity().getApplication()).getDb();
        contactDao = db.getContactDao();
        chatDao = db.getChatDao();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);

        getActivity().supportInvalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Contacts");


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactAdapter(getContext(),setData());
        mRecyclerView.setAdapter (mAdapter);


        return view;
    }


    public List<Contact> setData(){
        if (!contactDao.getAllContacts().isEmpty())
            return dataList = contactDao.getAllContacts();
        else return dataList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.update(contactDao.getAllContacts());
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_add, menu);
    }
}
