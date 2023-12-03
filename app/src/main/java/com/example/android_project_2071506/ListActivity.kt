package com.example.android_project_2071506

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private var adapter: MyAdapter? = null

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        supportActionBar?.apply {
            title = "판매 글 목록"
        }



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val filteringCheck = findViewById<CheckBox>(R.id.filteringCheck)
        val addBtn = findViewById<FloatingActionButton>(R.id.addBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        val massageBtn = findViewById<Button>(R.id.massageBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        recyclerView.adapter = adapter


        val listLayout = findViewById<ConstraintLayout>(R.id.listLayout)

        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    Firebase.auth.signOut()
                    finish()
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@ListActivity, "한 번 더 누르면 로그아웃 후 종료합니다.", Toast.LENGTH_SHORT).show()
                    listLayout.postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)




        filteringCheck.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                filterUpdateList()
            }else{
                updateList()
            }
        }

        updateList()

        addBtn.setOnClickListener {
            startActivity(
                Intent(this, DetailActivity::class.java)
            )
            finish()
        }

        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        }

        massageBtn.setOnClickListener {
            startActivity(
                Intent(this, MessageboxActivity::class.java)
            )
            finish()

        }
    }

    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }

    private fun filterUpdateList() {
        itemsCollectionRef.whereEqualTo("remain", "판매 중").get()
            .addOnSuccessListener {
                val items = arrayListOf<Item>()
                for (doc in it) {
                    items.add(Item(doc))
                }
                adapter?.updateList(items)
            }
    }




}