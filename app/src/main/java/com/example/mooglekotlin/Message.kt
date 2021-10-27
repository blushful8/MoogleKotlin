package com.example.mooglekotlin

import android.annotation.SuppressLint
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
data class Message(val message: String? = null, val name: String? = null, val time: String? = null) {

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

}