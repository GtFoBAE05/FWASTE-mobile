package com.example.ta_mobile.utils

import java.util.UUID

class Session {
    var id: String = UUID.randomUUID().toString()
}

class SessionConsumer(private val session: Session){

    fun getSessionId() = session.id

}

class SessionActivity {
    val id: String = UUID.randomUUID().toString()
}