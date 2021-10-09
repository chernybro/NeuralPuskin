package com.uralsiberianworks.neuralpushkin.chatsRoom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.MainActivity
import com.uralsiberianworks.neuralpushkin.R
import com.uralsiberianworks.neuralpushkin.chatsRoom.ChatsAdapter.ChatViewHolder.ClickListener
import com.uralsiberianworks.neuralpushkin.conversationRoom.Conversation

class FragmentChats : Fragment(), ClickListener {
    private var mAdapter: ChatsAdapter? = null
    override fun onCreate(a: Bundle?) {
        super.onCreate(a)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chats, null, false)
        requireActivity().invalidateOptionsMenu()
        (activity as MainActivity?)!!.setupToolbar(R.id.toolbar, "Messages")
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ChatsAdapter(requireContext(), this)
        recyclerView.adapter = mAdapter
        mAdapter!!.updateLastMessage()
        return view
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(activity, Conversation::class.java)
        intent.putExtra(POSITION, position)
        startActivity(intent)
    }

    override fun onItemLongClicked(position: Int): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        mAdapter!!.updateLastMessage()
    }

    companion object {
        private const val POSITION = "position"
    }
}