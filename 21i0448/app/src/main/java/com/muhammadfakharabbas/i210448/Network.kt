package com.muhammadfakharabbas.i210448

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class Network : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }


}