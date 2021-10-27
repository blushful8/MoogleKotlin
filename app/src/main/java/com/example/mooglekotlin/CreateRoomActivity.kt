package com.example.mooglekotlin

import android.R.attr.label
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mooglekotlin.databinding.ActivityCreateRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class CreateRoomActivity : AppCompatActivity() {
    private var binding: ActivityCreateRoomBinding? = null


    private var value: String? = null

    val database =
        Firebase.database("https://moogle-kotlin-e0a9f-default-rtdb.firebaseio.com/")
            .getReference("Rooms")


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
        binding = ActivityCreateRoomBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.createButton?.setOnClickListener {
            val key = database.push().key
            binding?.roomId?.text = key.toString()

            database.child(key.toString()).push().setValue(
                Message(
                    "Чат створив(ла) $value",
                    "Support by Firebase"
                )
            )

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label.toString(), binding?.roomId?.text.toString())
            clipboard.setPrimaryClip(clip)
            intent.putExtra("Id", clip)
        }

        binding?.roomId?.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label.toString(), binding?.roomId?.text.toString())
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "RoomId is successful copy", Toast.LENGTH_SHORT).show()
        }

    }
}