package com.example.mooglekotlin

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(val message: String? = null, val name: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.

}