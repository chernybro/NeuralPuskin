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
import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.database.Contact;
import com.uralsiberianworks.neuralpushkin.database.ContactDao;
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase;

import java.util.List;


public class FragmentContacts extends Fragment {

    private ContactAdapter mAdapter;
    private ContactDao contactDao;

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
        NeuralDatabase db = ((NeuralApp) getContext().getApplicationContext()).getDb();
        contactDao = db.getContactDao();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactAdapter(getContext(),setData());
        recyclerView.setAdapter (mAdapter);


        return view;
    }


    public List<Contact> setData(){
        return contactDao.getAllContacts();
    }

    @Override
    public void onStart() {
        super.onStart();
         mAdapter.update(contactDao.getAllContacts());
    }

}
