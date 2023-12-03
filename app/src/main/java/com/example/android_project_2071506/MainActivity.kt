package com.example.android_project_2071506

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signinBtn = findViewById<Button>(R.id.signinBtn)
        val signupBtn = findViewById<Button>(R.id.signupBtn)


            supportActionBar?.apply {
                title = "상상마켓"
            }

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)

        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                } else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@MainActivity, "한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
                    mainLayout.postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)



            signupBtn.setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java)
            )
            finish()
        }

        signinBtn.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }


    }
}