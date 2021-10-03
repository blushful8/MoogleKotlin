package com.example.mooglekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.mooglekotlin.FirebaseAuth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {


    var nameId: TextView? = null
    var email: TextView? = null
    var userName:TextView? = null

    private var mAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase : FirebaseDatabase? = null

    var nameIdString: String? = null
    var emailId: String? = null
    var userIdString: String? = null

    var Logout: AppCompatButton? = null
    var chat: ImageButton? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
        setValueFields()

        mDatabase = Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        Logout?.setOnClickListener{
            // Logout
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            finish()
        }
        chat?.setOnClickListener{
            intent = Intent(this@ProfileActivity, MessageActivity::class.java)
            startActivity(intent)

        }
    }

    fun init(){
        userIdString = intent.getStringExtra("userId")
        emailId = intent.getStringExtra("emailId")
        nameIdString = intent.getStringExtra("userNameId")

        userName = findViewById(R.id.tv_name)
        email = findViewById(R.id.tv_emailId)
        nameId = findViewById(R.id.tv_userId)

        Logout = findViewById(R.id.btn_Logout)
        chat = findViewById(R.id.btn_chat)
    }

    fun setValueFields(){
        email?.setText(emailId)
        nameId?.setText(userIdString)
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userName?.text = snapshot.child("userName").value as String
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}