package com.st.slex.common.messenger.utilites

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AppValueEventListener(val onSuccess: (DataSnapshot) -> Unit) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        Log.i("AppValueEventListener", snapshot.toString())
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.i("AppValueEventListener", error.details)
    }

}