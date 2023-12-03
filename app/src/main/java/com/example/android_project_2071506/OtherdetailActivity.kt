package com.example.android_project_2071506

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat

class OtherdetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otherdetail)

        val messageBtn = findViewById<Button>(R.id.messageBtn)
        val otherTitleText = findViewById<TextView>(R.id.otherTitleText)
        val otherPriceText = findViewById<TextView>(R.id.otherPriceText)
        val otherEmailText = findViewById<TextView>(R.id.otherEmailText)
        val otherDetailsText = findViewById<TextView>(R.id.otherDetailsText)
        val otherRemainText = findViewById<TextView>(R.id.otherRemainText)

        val intentOtherPrice = intent.getIntExtra("otherPrice", 0).toInt()
        val intentOtherEmail = intent.getStringExtra("otherEmail")
        val intentOtherDetails = intent.getStringExtra("otherDetails")
        val intentOtherRemain = intent.getStringExtra("otherRemain")
        val intentOtherTitle = intent.getStringExtra("otherTitle")
        val intentOtherDocID = intent.getStringExtra("otherdocID")
        val intentOtherUserID = intent.getStringExtra("otherUserID")

        val modiPrice = "${intentOtherPrice.toString()}원"
        otherTitleText.text = intentOtherTitle
        otherEmailText.text = intentOtherEmail
        otherDetailsText.text = intentOtherDetails
        otherPriceText.text = modiPrice
        otherRemainText.text = intentOtherRemain

        supportActionBar?.apply {
            title = "판매 글 보기"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@OtherdetailActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)




        messageBtn.setOnClickListener {
            val intent = Intent(it.context, sendActivity::class.java)

            intent.putExtra("sellerEmail", intentOtherEmail)
            intent.putExtra("sellerPrice", intentOtherPrice)
            intent.putExtra("sellerTitle", intentOtherTitle)
            intent.putExtra("sellerDocID", intentOtherDocID)
            intent.putExtra("sellerUserID", intentOtherUserID)


            ContextCompat.startActivity(it.context, intent, null)
            finish()
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