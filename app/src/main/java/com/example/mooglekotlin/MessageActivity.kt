package com.example.mooglekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MessageActivity : AppCompatActivity() {

    private lateinit var clickMessage: FloatingActionButton
    private lateinit var textInput: EditText
    val database = Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        init()
        clickFab()

    }

    fun init(){
        clickMessage = findViewById(R.id.btn_FAB)
        textInput = findViewById(R.id.et_message)



    }
    fun writeNewMessage(userId: String, message: String, name: String) {
        val message = Message(name, message)

        database.child("messages").child(userId).setValue(message)
    }


    fun clickFab(){

        val textMessage = Message()
        textMessage.message
        clickMessage.setOnClickListener {
            database.child("message").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<String>()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}