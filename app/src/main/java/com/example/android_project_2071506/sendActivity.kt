package com.example.android_project_2071506

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class sendActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val intentSellerPrice = intent.getIntExtra("sellerPrice", 0).toInt()
        val intentSellerEmail = intent.getStringExtra("sellerEmail")
        val intentSellerTitle = intent.getStringExtra("sellerTitle")
        val intentSellerDocID = intent.getStringExtra("sellerDocID")
        val intentSellerUserID = intent.getStringExtra("sellerUserID")

        val sendReceiverText = findViewById<TextView>(R.id.sendReceiverText)
        val sendTitleText = findViewById<TextView>(R.id.sendTitleText)
        val sendPriceText = findViewById<TextView>(R.id.sendPricetext)
        val sendDetailsText = findViewById<EditText>(R.id.sendDetailsText)
        val sendSendBtn = findViewById<Button>(R.id.sendSendBtn)

        sendReceiverText.text = intentSellerEmail
        sendTitleText.text = intentSellerTitle
        val modiPrice = "${intentSellerPrice.toString()}원"
        sendPriceText.text = modiPrice

        supportActionBar?.apply {
            title = "메시지 보내기"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@sendActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val touchView = findViewById<ConstraintLayout>(R.id.sendLayout)
        touchView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        sendSendBtn.setOnClickListener {
            if(sendDetailsText.text.toString() == ""){
                val snackbar = Snackbar.make(touchView, "보내실 메시지 내용을 입력하세요!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }else{
                val messageContent = sendDetailsText.text.toString()
                val itemMap = hashMapOf(
                    "content" to messageContent,
                    "sender" to Firebase.auth.currentUser?.email.toString()
                )


                var cnt: Int = 0
                if(intentSellerUserID != null){
                    usersCollectionRef.document(intentSellerUserID).collection("message").get().addOnSuccessListener {
                        cnt = it.size()
                        usersCollectionRef.document(intentSellerUserID).collection("message").document(cnt.toString()).set(itemMap).addOnSuccessListener {
                            startActivity(
                                Intent(this, ListActivity::class.java)
                            )
                            finish()

                        }.addOnFailureListener {
                        }
                    }
            }



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
