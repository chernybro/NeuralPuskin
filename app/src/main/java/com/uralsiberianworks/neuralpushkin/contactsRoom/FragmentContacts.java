package com.uralsiberianworks.neuralpushkin.contactsRoom;


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


public class FragmentContacts extends Fragment {

    private ContactAdapter mAdapter;

    public FragmentContacts(){ }


    public void onCreate(Bundle a){
        super.onCreate(a);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);

        getActivity().invalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Contacts");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactAdapter(getContext());
        recyclerView.setAdapter (mAdapter);
        setData();

        return view;
    }


    public void setData(){
         mAdapter.update();
    }

    @Override
    public void onStart() {
        super.onStart();
         mAdapter.update();
    }

}
