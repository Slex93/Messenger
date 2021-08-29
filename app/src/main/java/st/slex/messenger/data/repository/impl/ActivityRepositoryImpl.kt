package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : ActivityRepository {

    override suspend fun isAuthorise(): Flow<Resource<FirebaseAuth>> = callbackFlow {
        val auth = FirebaseAuth.getInstance()
        val event = if (auth.currentUser != null) {
            trySendBlocking(Resource.Success(auth)).isSuccess
        } else {
            trySendBlocking(Resource.Failure(Exception("Current user is null"))).isFailure
        }
        awaitClose { event }
    }

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

    override suspend fun updatePhonesToDatabase(listContact: List<ContactModel>) =
        withContext(Dispatchers.IO) {
            databaseReference.child(NODE_PHONE)
                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                    dataSnapshot.children.forEach { snapshot ->
                        listContact.forEach { contact ->
                            if (auth.uid.toString() != snapshot.key && snapshot.value == contact.phone) {
                                databaseReference.child(NODE_PHONE_CONTACT)
                                    .child(auth.uid.toString())
                                    .child(snapshot.value.toString()).child(CHILD_ID)
                                    .setValue(snapshot.key.toString())
                                databaseReference.child(NODE_PHONE_CONTACT)
                                    .child(auth.uid.toString())
                                    .child(snapshot.value.toString()).child(auth.uid.toString())
                                    .setValue(contact.fullname)
                            }
                        }
                    }
                })
        }
}