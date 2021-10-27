package com.example.mooglekotlin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mooglekotlin.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    private var adapter: MessageAdapter? = null
    // init binding
    private var binding: ActivityChatBinding? = null

    var UserName: String? = null

    val database =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/").reference
    // Firebase work with path "Users"
    val mNamedatabase =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
            .getReference("Users")

    // init auth and check when user don't sign in yet
    val auth = FirebaseAuth.getInstance()
    val mUser = auth.currentUser
    val mUserReference = mNamedatabase.child(mUser!!.uid)
    val name = mUserReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // get value from Firebase
            UserName = snapshot.child("userName").value as String

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // work with binding
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRcView()
        // visible chat from Firebase
        onChangeListener(database)

        binding?.btnFAB?.setOnClickListener {
            if (binding?.etMessage?.length()!! > 0) {
                val message = binding?.etMessage?.text.toString()
                database.child("Messages").child(database.push().key ?: "message").setValue(
                    Message(
                        message = message,
                        name = UserName
                    )
                )
                binding?.etMessage?.setText("")
            } else {
                Toast.makeText(this, "You are trying input empty text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun initRcView() = with(binding) {
        adapter = MessageAdapter()
        this?.recycler?.layoutManager = LinearLayoutManager(this@ChatActivity)
        this!!.recycler.adapter = adapter
    }


    private fun onChangeListener(dref: DatabaseReference) {
        dref.child("Messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Message>()
                for (s in snapshot.children) {
                    val message = s.getValue(Message::class.java)
                    if (message != null) {
                        list.add(message)
                    }
                }
                adapter?.submitList(list)
                binding?.recycler?.smoothScrollToPosition(list.size)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}