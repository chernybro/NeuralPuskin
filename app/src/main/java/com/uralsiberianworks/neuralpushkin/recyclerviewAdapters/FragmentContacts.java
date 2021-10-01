package com.uralsiberianworks.neuralpushkin.recyclerviewAdapters;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uralsiberianworks.neuralpushkin.MainActivity;
import com.uralsiberianworks.neuralpushkin.NeuralApp;
import com.uralsiberianworks.neuralpushkin.R;
import com.uralsiberianworks.neuralpushkin.db.Chat;
import com.uralsiberianworks.neuralpushkin.db.Contact;
import com.uralsiberianworks.neuralpushkin.db.ContactDao;
import com.uralsiberianworks.neuralpushkin.db.NeuralDatabase;

import java.util.ArrayList;
import java.util.List;


public class FragmentContacts extends Fragment {

    private ContactAdapter mAdapter;
    private ContactDao contactDao;
    private NeuralDatabase db;

    public FragmentContacts(){
        setHasOptionsMenu(true);
    }

    public FragmentContacts(NeuralDatabase db) {
        this.db = db;

        setHasOptionsMenu(true);
    }

    public void onCreate(Bundle a){
        super.onCreate(a);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null, false);

        getActivity().supportInvalidateOptionsMenu();
        ((MainActivity)getActivity()).setupToolbar(R.id.toolbar, "Contacts");

        contactDao = db.getContactDao();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ContactAdapter(getContext());
        recyclerView.setAdapter (mAdapter);
        setData1();

        return view;
    }

    public void setData1(){
        ArrayList<Contact> contacts= new ArrayList<>();
        String imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.push6).toString();
        contacts.add(new Contact("1","bla", imageUri));
        mAdapter.update(contacts);
    }

    public void setData(){
        mAdapter.update(contactDao.getAllContacts());
    }

    @Override
    public void onStart() {
        super.onStart();
      //  mAdapter.update(contactDao.getAllContacts());
    }

}
