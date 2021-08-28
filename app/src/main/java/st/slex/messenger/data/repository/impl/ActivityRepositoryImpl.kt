package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.utilites.Const.AUTH
import st.slex.messenger.utilites.Const.CHILD_FULLNAME
import st.slex.messenger.utilites.Const.CHILD_ID
import st.slex.messenger.utilites.Const.CHILD_STATE
import st.slex.messenger.utilites.Const.CURRENT_UID
import st.slex.messenger.utilites.Const.NODE_PHONE
import st.slex.messenger.utilites.Const.NODE_PHONE_CONTACT
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT
import st.slex.messenger.utilites.Const.USER
import st.slex.messenger.utilites.base.AppValueEventListener
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor() : ActivityRepository {

    override suspend fun initFirebase() = withContext(Dispatchers.IO) {
        AUTH = FirebaseAuth.getInstance()
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        USER = UserModel()
        CURRENT_UID = AUTH.currentUser?.uid.toString()
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        AUTH.signOut()
    }

    override suspend fun statusOnline(): Unit = withContext(Dispatchers.IO) {
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .child(CHILD_STATE).setValue("Online")
    }

    override suspend fun statusOffline(): Unit = withContext(Dispatchers.IO) {
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
            .child(CHILD_STATE).setValue("Offline")
    }

    override suspend fun updatePhonesToDatabase(listContact: List<ContactModel>) =
        withContext(Dispatchers.IO) {
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
}