package com.example.android_project_2071506

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private val usersCollectionRef = db.collection("users")
    private lateinit var currentUserEmail: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailTitle = findViewById<EditText>(R.id.detailTitleText)
        val detailPrice = findViewById<EditText>(R.id.detailPriceText)
        val detailDetails = findViewById<EditText>(R.id.detailDetailsText)
        val registBtn = findViewById<Button>(R.id.RegistBtn)
        val detailLayout = findViewById<ConstraintLayout>(R.id.detailLayout)

        supportActionBar?.apply {
            title = "판매 글 등록"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        detailLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        usersCollectionRef.document(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener {
            currentUserEmail = it["email"].toString()
        }

        registBtn.setOnClickListener {

            if(detailDetails.text.toString() != "" && detailPrice.text.toString() != "" && detailTitle.text.toString() != ""){
                val docRef = itemsCollectionRef.document()
                val docID = docRef.id

                val itemMap = hashMapOf(
                    "title" to detailTitle.text.toString(),
                    "price" to detailPrice.text.toString().toInt(),
                    "details" to detailDetails.text.toString(),
                    "remain" to "판매 중",
                    "email" to currentUserEmail,
                    "docID" to docID,
                    "UserID" to Firebase.auth.currentUser?.uid.toString()

                )

                docRef.set(itemMap).addOnSuccessListener {
                    startActivity(
                        Intent(this, ListActivity::class.java)
                    )
                    finish()
                }.addOnFailureListener{
                    println("#####bug#####")
                }
            }else{
                val snackbar = Snackbar.make(detailLayout, "제목, 가격, 내용을 모두 입력하세요!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }



        }
    }
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
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
