package com.uralsiberianworks.neuralpushkin.contactsRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.MainActivity
import com.uralsiberianworks.neuralpushkin.R

class FragmentContacts : Fragment() {
    private var mAdapter: ContactAdapter? = null
    override fun onCreate(a: Bundle?) {
        super.onCreate(a)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, null, false)
        requireActivity().invalidateOptionsMenu()
        (activity as MainActivity?)!!.setupToolbar(R.id.toolbar, "Contacts")
        val recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ContactAdapter(requireContext())
        recyclerView.adapter = mAdapter
        setData()
        return view
    }

    fun setData() {
        mAdapter!!.update()
    }

    override fun onStart() {
        super.onStart()
        mAdapter!!.update()
    }
}