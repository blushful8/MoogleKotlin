package com.example.mooglekotlin.FirebaseAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.mooglekotlin.ProfileActivity
import com.example.mooglekotlin.R
import com.example.webViewkotlin.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private var ok: AppCompatButton? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var register: TextView? = null
    private var backMain: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        if (mAuth!!.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            val intent = Intent(this, ProfileActivity::class.java);
            startActivity(intent);
            finish();
        }

        clickButton()

        register?.setOnClickListener{
            intent  = Intent(this@LoginActivity, RegistrationActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // data transfer
            // start register activity
            startActivity(intent)
            finish()
        }
    }


    fun init(){
        ok = findViewById(R.id.btn_ok)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        register = findViewById(R.id.tv_reg)
        backMain = findViewById(R.id.btn_back_main)

        mDatabaseReference = Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/").reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

    }

    fun clickButton() {

        ok?.setOnClickListener {
            when {
                // check all field


                TextUtils.isEmpty(email?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Email is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(password?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Password is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    // initial value string and appropriation equal values
                    val email: String = email?.text.toString().trim { it <= ' '}
                    val password: String = password?.text.toString().trim { it <= ' '}

                    mAuth!!.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                        // if register user result is successful done
                        if (task.isSuccessful) {

                            Toast.makeText(
                                this@LoginActivity,
                                "You are success sign in your profile $email",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Sign in success, update UI with the signed-in user's information

                            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            // data transfer
                            intent.putExtra("userId", FirebaseAuth.getInstance().currentUser!!.uid)
                            intent.putExtra("emailId", email)

                            // start profile activity
                            startActivity(intent)
                            finish()

                        }else{
                            // if login don't successful
                            Toast.makeText(
                                this@LoginActivity,
                                "Error:" + task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
                }
            }


        }

        backMain?.setOnClickListener{
            intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}