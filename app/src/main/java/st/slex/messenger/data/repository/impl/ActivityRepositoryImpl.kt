package st.slex.messenger.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.EventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityRepositoryImpl @Inject constructor(
    private val service: DatabaseSnapshot,
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : ActivityRepository {

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        auth.signOut()
    }

    override suspend fun statusOnline(): Unit = withContext(Dispatchers.IO) {
        databaseReference.child(NODE_USER).child(auth.uid.toString())
            .child(CHILD_STATE).setValue("Online")
    }

    override suspend fun statusOffline(): Unit = withContext(Dispatchers.IO) {
        databaseReference.child(NODE_USER).child(auth.uid.toString())
            .child(CHILD_STATE).setValue("Offline")
    }

    override suspend fun updateContacts(list: List<ContactModel>) = withContext(Dispatchers.IO) {
        service.singleValueEventFlow(databaseReference.child(NODE_PHONE)).collect {
            when (it) {
                is EventResponse.Success -> {
                    it.snapshot.children.forEach { snapshot ->
                        list.forEach { contact ->
                            if (auth.currentUser?.uid.toString() != snapshot.key && snapshot.value == contact.phone) {
                                service.singleValueEventFlow(
                                    databaseReference.child(NODE_USER).child(
                                        snapshot.key.toString()
                                    )
                                ).collect { rawUser ->
                                    when (rawUser) {
                                        is EventResponse.Success -> {
                                            val user = rawUser.snapshot.getThisValue<UserModel>()
                                            val refContact =
                                                "$NODE_CONTACT/${auth.currentUser?.uid}/${snapshot.value}"
                                            val mapUser = mapOf(
                                                CHILD_ID to user.id,
                                                CHILD_PHONE to user.phone,
                                                CHILD_USERNAME to user.username,
                                                CHILD_URL to user.url,
                                                CHILD_BIO to user.bio,
                                                CHILD_FULL_NAME to user.full_name,
                                                CHILD_STATE to user.state
                                            )
                                            val mapContact = mapOf(refContact to mapUser)
                                            databaseReference
                                                .updateChildren(mapContact)
                                        }
                                        is EventResponse.Cancelled -> {
                                            Log.e("$this", rawUser.databaseError.toString())
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                is EventResponse.Cancelled -> {
                    Log.e("$this", it.databaseError.toString())
                }
            }
        }
    }
}