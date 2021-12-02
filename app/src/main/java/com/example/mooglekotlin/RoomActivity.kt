package com.example.mooglekotlin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mooglekotlin.databinding.ActivityRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class RoomActivity : AppCompatActivity() {

    private var binding: ActivityRoomBinding? = null
    private var UserName: String? = null
    private var adapter: MessageAdapter? = null
    var id: String? = null

    val database =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
            .reference

    val mNamedatabase =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
            .getReference("Users")
    val auth = FirebaseAuth.getInstance()
    val mUser = auth.currentUser
    val mUserReference = mNamedatabase.child(mUser!!.uid)
    val name = mUserReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            UserName = snapshot.child("userName").value as String

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent.getStringExtra("Id")


        initRcView()
        onChangeListener(database)


        binding?.btnFAB?.setOnClickListener {
            if (binding?.etMessage?.length()!! > 0) {
                val message = binding?.etMessage?.text.toString()
                database.child("Rooms").child(id!!).child(database.push().key ?: "message").setValue(
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
        this?.recycler?.layoutManager = LinearLayoutManager(this@RoomActivity)
        this!!.recycler.adapter = adapter
    }

    private fun onChangeListener(dref: DatabaseReference) {
        dref.child("Rooms").child(id!!).addValueEventListener(object : ValueEventListener {
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