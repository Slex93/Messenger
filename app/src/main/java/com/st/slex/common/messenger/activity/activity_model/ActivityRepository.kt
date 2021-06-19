package com.st.slex.common.messenger.activity.activity_model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ActivityRepository {

    fun initFirebase(){
        AUTH = FirebaseAuth.getInstance()
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        USER = User()
        CURRENT_UID = AUTH.currentUser?.uid.toString()
    }

    fun signOut(){
        AUTH.signOut()
    }

}