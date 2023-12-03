package com.example.android_project_2071506

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MydetailActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mydetail)

        val myPriceText = findViewById<EditText>(R.id.myPriceText)
        val myRemainCheck = findViewById<CheckBox>(R.id.myRemainCheck)
        val modiBtn = findViewById<Button>(R.id.modiBtn)

        val docID = intent.getStringExtra("docID")
        val intentPrice = intent.getIntExtra("price", 0).toString()
        val intentRemain = intent.getStringExtra("remain")
        myPriceText.setText(intentPrice)

        supportActionBar?.apply {
            title = "판매 글 수정"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MydetailActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        lateinit var modiRemain: String

        modiRemain = if(intentRemain == "판매 완료"){
            myRemainCheck.isChecked = true
            "판매 완료"
        }else{
            "판매 중"
        }

        myRemainCheck.setOnCheckedChangeListener { _, isChecked ->
            modiRemain = if(isChecked){
                "판매 완료"
            }else{
                "판매 중"
            }
        }

        modiBtn.setOnClickListener {
            val modiPrice = myPriceText.text.toString().toInt()
            if (docID != null) {
                if (intentRemain != null) {
                    updateData(docID, modiPrice, modiRemain )
                }
            }else{
                println("error")
            }
        }


    }

    private fun updateData(docID: String, price: Int, remain: String){
        itemsCollectionRef.document(docID).update("price", price, "remain", remain)
            .addOnSuccessListener {
                println("#####updated!!!!#####")
                println(docID)
                println(price)
                println(remain)
                startActivity(
                    Intent(this, ListActivity::class.java)
                )
                finish()
            }.addOnFailureListener{
                println("#######modi Fail#####")
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