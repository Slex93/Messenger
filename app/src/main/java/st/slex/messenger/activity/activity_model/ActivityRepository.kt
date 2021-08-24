package st.slex.messenger.activity.activity_model


import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import st.slex.common.messenger.activity.activity_model.ActivityConst.AUTH
import st.slex.common.messenger.activity.activity_model.ActivityConst.CHILD_FULLNAME
import st.slex.common.messenger.activity.activity_model.ActivityConst.CHILD_ID
import st.slex.common.messenger.activity.activity_model.ActivityConst.CHILD_STATE
import st.slex.common.messenger.activity.activity_model.ActivityConst.CHILD_USERNAME
import st.slex.common.messenger.activity.activity_model.ActivityConst.CURRENT_UID
import st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_PHONE
import st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_PHONE_CONTACT
import st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_USER
import st.slex.common.messenger.activity.activity_model.ActivityConst.REF_DATABASE_ROOT
import st.slex.common.messenger.activity.activity_model.ActivityConst.USER
import st.slex.common.messenger.utilites.AppValueEventListener

class ActivityRepository {

    val getUserForHeader = MutableLiveData<User>()

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
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty()) {
                    USER = User(CURRENT_UID, USER.phone, USER.username, USER.url)
                    REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_USERNAME)
                        .setValue(CURRENT_UID)
                }
                getUserForHeader.value = USER
            })
    }

    fun updatePhonesToDatabase(listContact: List<Contact>) {
        REF_DATABASE_ROOT.child(NODE_PHONE)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                dataSnapshot.children.forEach { snapshot ->
                    listContact.forEach { contact ->
                        if (CURRENT_UID != snapshot.key && snapshot.value == contact.phone) {
                            REF_DATABASE_ROOT.child(NODE_PHONE_CONTACT).child(CURRENT_UID)
                                .child(snapshot.value.toString()).child(CHILD_ID)
                                .setValue(snapshot.key.toString())
                            REF_DATABASE_ROOT.child(NODE_PHONE_CONTACT).child(CURRENT_UID)
                                .child(snapshot.value.toString()).child(CHILD_FULLNAME)
                                .setValue(contact.fullname)
                        }
                    }
                }
            })
    }

    fun statusOnline(){
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_STATE).setValue("Online")
    }

    fun statusOffline(){
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).child(CHILD_STATE).setValue("Offline")
    }

}