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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {



    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val loginEmail = findViewById<EditText>(R.id.loginEmailText)
        val loginPassword = findViewById<EditText>(R.id.loginPasswordText)
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        val touchView = findViewById<ConstraintLayout>(R.id.loginLayout)
        touchView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        supportActionBar?.apply {
            title = "로그인"
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)


        loginBtn.setOnClickListener {
            if(loginEmail.text.toString() != "" && loginPassword.text.toString() !=""){
                doLogin(loginEmail.text.toString(), loginPassword.text.toString(), touchView)
            }else{
                val snackbar = Snackbar.make(touchView, "이메일과 패스워드를 입력하세요!", Snackbar.LENGTH_LONG)
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


    private fun doLogin(userEmail:String, userPassword:String, touchView:ConstraintLayout){
        Firebase.auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this){
            if(it.isSuccessful){
                startActivity(
                    Intent(this, ListActivity::class.java)
                )
                finish()
            }else{
                val snackbar = Snackbar.make(touchView, "이메일과 패스워드를 확인하세요!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}