package com.example.mooglekotlin.FirebaseAuth

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.mooglekotlin.ProfileActivity
import com.example.mooglekotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class RegistrationActivity : AppCompatActivity() {

    private var ok: AppCompatButton? = null
    private var name: EditText? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var login: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
        clickButton()

        login?.setOnClickListener{
            val  intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // finish this activity
            finish()
        }
    }


    fun init(){
        ok = findViewById(R.id.btn_ok)
        name = findViewById(R.id.et_firstName)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        login = findViewById(R.id.tv_log)

        mDatabaseReference = Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/").reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

    }

    fun clickButton() {

        ok?.setOnClickListener {
            when {
                // check all field
                TextUtils.isEmpty(name?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Name is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(email?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Email is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(password?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Password is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    // initial value string and appropriation equal values
                    val name: String = name?.text.toString().trim { it <= ' '}
                    val email: String = email?.text.toString().trim { it <= ' '}
                    val password: String = password?.text.toString().trim { it <= ' '}

                    mAuth!!.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                // if register user result is successful done
                                if (task.isSuccessful) {

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                                    val userId = mAuth!!.currentUser!!.uid
                                    //Verify Email
                                    verifyEmail();

                                    //update user profile information
                                    val currentUserDb = mDatabaseReference?.child(userId)
                                    currentUserDb?.child("userName")?.setValue(name)
                                    currentUserDb?.child("email")?.setValue(email)
                                    currentUserDb?.child("password")?.setValue(password)

                                    // Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!



                                    val intent = Intent(this@RegistrationActivity, ProfileActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    // data transfer
                                    intent.putExtra("userId", firebaseUser.uid)
                                    intent.putExtra("emailId", email)
                                    intent.putExtra("userNameId", name)
                                    // start chat activity
                                    startActivity(intent)
                                    finish()
                                }else{
                                    // if register don't successful
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Error:" + task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            })
                }
            }


        }
    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@RegistrationActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(ContentValues.TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this@RegistrationActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}