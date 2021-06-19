package com.st.slex.common.messenger.activity.activity_model



import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    fun initUser() {
        /*REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty()) {
                    USER = User(CURRENT_UID, USER.phone, USER.username, USER.url)
                    REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_USERNAME)
                        .setValue(CURRENT_UID)
                }
            })*/

        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("AppValueEventListener", snapshot.toString())
                    USER = snapshot.getValue(User::class.java) ?: User()
                    if (USER.username.isEmpty()) {
                        USER = User(CURRENT_UID, USER.phone, USER.username, USER.url)
                        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_USERNAME)
                            .setValue(CURRENT_UID)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}