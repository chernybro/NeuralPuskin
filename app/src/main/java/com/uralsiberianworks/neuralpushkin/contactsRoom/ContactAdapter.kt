package com.uralsiberianworks.neuralpushkin.contactsRoom

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.uralsiberianworks.neuralpushkin.NeuralApp
import com.uralsiberianworks.neuralpushkin.R
import com.uralsiberianworks.neuralpushkin.SetContactActivity
import com.uralsiberianworks.neuralpushkin.database.Contact
import com.uralsiberianworks.neuralpushkin.database.NeuralDatabase
import java.io.File

class ContactAdapter(private val mContext: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private var mArrayList: MutableList<Contact?>?
    private val db: NeuralDatabase = (mContext.applicationContext as NeuralApp).db
    fun update() {
        mArrayList?.clear()
        mArrayList = db.contactDao?.allContacts
        notifyDataSetChanged()
    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_contact, null)
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvName.text = mArrayList?.get(position)?.name ?: "null"
        var recipientImagePath = mArrayList?.get(position)?.imagePath
        if (mArrayList?.get(position)?.contactID != "push") {
            var imgFile: File? = File(recipientImagePath)
            if (imgFile!!.exists()) {
                var myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                Log.i("TAG", "saveImage: " + imgFile.length())
                viewHolder.userPhoto.setImageBitmap(myBitmap)
                imgFile = null
                myBitmap = null
                recipientImagePath = null
            }
        }
        val id = mArrayList?.get(position)?.contactID
        viewHolder.delBtn.setOnClickListener { view: View? ->
            val intent = Intent(mContext, SetContactActivity::class.java)
            intent.putExtra(CONTACT_DEL, id)
            if (id == "push") {
                Toast.makeText(mContext, "Оставьте Пушкина в покое", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(mContext)
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton(android.R.string.yes) { dialogInterface: DialogInterface?, i: Int ->
                            //mArrayList.remove(position);
                            db.contactDao?.del(id)
                            //mContext.startActivity(intent);
                            update()
                        }
                        .setNegativeButton(android.R.string.no) { dialogInterface: DialogInterface?, i: Int -> }.show()
            }
        }
        viewHolder.editBtn.setOnClickListener { view: View? ->
            if (id == "push") {
                Toast.makeText(mContext, "Оставьте Пушкина в покое", Toast.LENGTH_SHORT).show()
            } else {
                val intent1 = Intent(mContext, SetContactActivity::class.java)
                intent1.putExtra(CONTACT_EDIT, id)
                mContext.startActivity(intent1)
                update()
            }
        }
    }

    override fun getItemCount(): Int {
        return mArrayList?.size!!
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val tvName: TextView = itemLayoutView.findViewById(R.id.tv_user_name)
        val userPhoto: ImageView = itemLayoutView.findViewById(R.id.iv_user_photo)
        val editBtn: ImageButton = itemLayoutView.findViewById(R.id.edit_btn)
        val delBtn: ImageButton = itemLayoutView.findViewById(R.id.del_btn)

    }

    companion object {
        private const val CONTACT_DEL = "contact_del"
        private const val CONTACT_EDIT = "contact_edit"
    }

    init {
        mArrayList = db.contactDao?.allContacts
        notifyDataSetChanged()
    }
}