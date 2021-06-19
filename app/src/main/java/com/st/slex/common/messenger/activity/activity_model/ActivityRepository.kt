package com.st.slex.common.messenger.activity.activity_model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.AUTH
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.CHILD_USERNAME
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.CURRENT_UID
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_USER
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.REF_DATABASE_ROOT
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.USER


import com.st.slex.common.messenger.utilites.AppValueEventListener

class ActivityRepository {

    fun initFirebase() {
        AUTH = FirebaseAuth.getInstance()
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        USER = User()
        CURRENT_UID = AUTH.currentUser?.uid.toString()
    }

    fun signOut() {
        AUTH.signOut()
    }

    inline fun initUser(crossinline function: () -> Unit) {
        Log.i("UserRepoPre", USER.toString())

        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty()) {
                    USER = User(CURRENT_UID, USER.phone, USER.username, USER.url)
                    Log.i("UserIn", USER.toString())
                    REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_USERNAME)
                        .setValue(CURRENT_UID)
                    function()
                }
            })
        Log.i("UserRepoPost", USER.toString())

    }

}