package com.example.mooglekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mooglekotlin.databinding.ActivityChatBinding
import com.example.mooglekotlin.databinding.ActivityMainBinding
import com.example.mooglekotlin.databinding.ActivityMessageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var clickMessage: FloatingActionButton
    private lateinit var textInput: EditText
    private var binding: ActivityChatBinding? = null
    private var adapter: MessageAdapter? = null
    private var mDataRef: DatabaseReference? = null
    var userName = Message()
    var value: String? = null

    val database =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/").reference
    val mNamedatabase =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
            .getReference("Users")
    val auth = FirebaseAuth.getInstance()
    val mUser = auth!!.currentUser
    val mUserReference = mNamedatabase!!.child(mUser!!.uid)
    val name = mUserReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            value = snapshot.child("userName").value as String

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding?.root)




        init()
        initRcView()
        onChangeListener(database)

        binding?.btnFAB?.setOnClickListener {
            if (textInput.length() > 0) {
                database.child("Messages").child(database.push().key ?: "message").setValue(
                    Message(
                        textInput.text.toString(),
                        value
                    )
                )
                textInput.setText("")
            } else {
                Toast.makeText(this, "You are trying input empty text", Toast.LENGTH_SHORT).show()
            }


        }


    }

    fun init() {
        textInput = findViewById(R.id.et_message)

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