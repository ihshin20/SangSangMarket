package com.example.android_project_2071506

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageboxActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private val UsersCollectionRef = db.collection("users")
    private var adapter: MessageAdapter? = null
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messagebox)

        val messageRecyclerView = findViewById<RecyclerView>(R.id.messageRecyclerView)

        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, emptyList())
        messageRecyclerView.adapter = adapter

        supportActionBar?.apply {
            title = "메시지 보기"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MessageboxActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        updateList()
    }

    private fun updateList() {
        val currentUID = Firebase.auth.currentUser?.uid?.toString()
        if(currentUID != null){
            UsersCollectionRef.document(currentUID).collection("message").get().addOnSuccessListener {
                val items = mutableListOf<MessageItem>()
                for (doc in it) {
                    items.add(MessageItem(doc))
                }
                adapter?.updateList(items)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(this, ListActivity::class.java)
                )
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}