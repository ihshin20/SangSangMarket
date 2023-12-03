package com.example.android_project_2071506

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageItem(val sender: String, val content: String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc["sender"].toString(), doc["content"].toString())
}

class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MessageAdapter(private val context: Context, private var items: List<MessageItem>)
    : RecyclerView.Adapter<MessageViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    fun updateList(newList: List<MessageItem>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.messages, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val db: FirebaseFirestore = Firebase.firestore
        val itemsCollectionRef = db.collection("items")
        val usersCollectionRef = db.collection("users")
        lateinit var currentUserEmail: String

        val item = items[position]
        holder.view.findViewById<TextView>(R.id.senderEmailText).text = item.sender
        holder.view.findViewById<TextView>(R.id.messageContentText).text = item.content

    }

    override fun getItemCount() = items.size
}


