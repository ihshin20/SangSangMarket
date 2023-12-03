package com.example.android_project_2071506

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.MotionEvent
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

class SignupActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailText = findViewById<EditText>(R.id.emailText)
        val passwordText = findViewById<EditText>(R.id.passwordText)
        val passwordCheckText = findViewById<EditText>(R.id.passwordCheckText)
        val nameText = findViewById<EditText>(R.id.nameText)
        val birthText = findViewById<EditText>(R.id.birthText)
        val createBtn = findViewById<Button>(R.id.createBtn)
        val signuplayout = findViewById<ConstraintLayout>(R.id.signup_layout)

        supportActionBar?.apply {
            title = "계정 생성"
        }

        signuplayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)



        createBtn.setOnClickListener {

            if (isValidEmail(emailText.text.toString())) {
                if(passwordText.text.toString() != ""){
                    if(passwordText.text.toString() == passwordCheckText.text.toString()){
                        if(nameText.text.toString() !=""){
                            if (isValidBirthday(birthText.text.toString())) {
                                doSignup(emailText.text.toString(), passwordText.text.toString())
                            } else {
                                val snackbar = Snackbar.make(signuplayout, "생년월일을 확인하세요!", Snackbar.LENGTH_LONG)
                                snackbar.show()
                            }

                        }else{
                            val snackbar = Snackbar.make(signuplayout, "이름을 확인하세요!", Snackbar.LENGTH_LONG)
                            snackbar.show()
                        }

                    }else{
                        val snackbar = Snackbar.make(signuplayout, "패스워드가 불일치합니다!", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }

                }else{
                    val snackbar = Snackbar.make(signuplayout, "패스워드를 확인하세요!", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }


            } else {
                    val snackbar = Snackbar.make(signuplayout, "이메일 형식을 확인하세요!", Snackbar.LENGTH_LONG)
                    snackbar.show()

                }

        }



    }

    fun isValidBirthday(birthday: String?): Boolean {
        if (birthday == null || birthday.length != 6) {
            return false
        }

        // 숫자로만 구성되어 있는지 확인
        return birthday.all { it.isDigit() }
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun doSignup(userEmail: String, userPassword: String){


        Firebase.auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this){



            if(it.isSuccessful){

                val uid2email = Firebase.auth.currentUser?.uid.toString()
                val itemMap = hashMapOf(
                    "email" to userEmail
                )
                usersCollectionRef.document(uid2email).set(itemMap).addOnSuccessListener {
                    println("########email added")
                }.addOnFailureListener{
                    println("#####bug#####")
                }



                val snackbar1 = Snackbar.make(findViewById<ConstraintLayout>(R.id.signup_layout), "signup success", Snackbar.LENGTH_LONG)
                snackbar1.show()
                startActivity(
                    Intent(this, ListActivity::class.java)
                )
                finish()
            }else{
                println("failed")
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

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}