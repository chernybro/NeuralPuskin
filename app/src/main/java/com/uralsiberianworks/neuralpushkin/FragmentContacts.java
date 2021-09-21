package com.uralsiberianworks.neuralpushkin;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.recyclerviewChats.Contact;
import com.uralsiberianworks.neuralpushkin.recyclerviewChats.ContactAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentContacts extends Fragment implements ContactAdapter.ViewHolder.ClickListener{
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    public FragmentContacts(){
        setHasOptionsMenu(true);
    }
    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);

        getActivity().supportInvalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Contacts");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactAdapter(getContext(),setData(),this);
        mRecyclerView.setAdapter (mAdapter);

        return view;
    }
    public List<Contact> setData(){
        List<Contact> data = new ArrayList<>();
        String name[]= {"Alexander Pushkin", "Angela Price" };
        @DrawableRes int img[]= {R.drawable.userpic , R.drawable.push1};

        for (int i = 0; i<2; i++){
            Contact contact = new Contact();
            contact.setName(name[i]);
            contact.setImage(img[i]);
            data.add(contact);
        }
        return data;
    }

    @Override
    public void onItemClicked (int position) {

    }

    @Override
    public boolean onItemLongClicked (int position) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_add, menu);
    }
}
