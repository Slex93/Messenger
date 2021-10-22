package st.slex.messenger.data.main_activity

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.Resource
import st.slex.messenger.data.contacts.ContactData
import st.slex.messenger.utilites.CHILD_STATE
import st.slex.messenger.utilites.NODE_PHONE
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ActivityRepository {

    suspend fun changeState(state: String)
    suspend fun updateContacts(list: List<ContactData>): Flow<Resource<Nothing?>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser,
        private val updater: ActivityContactsUpdater
    ) : ActivityRepository {

        override suspend fun changeState(state: String): Unit = suspendCoroutine { continuation ->
            val stateReference = reference.child(NODE_USER).child(auth.uid).child(CHILD_STATE)
            val task = stateReference.setValue(state)
            task.addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

        override suspend fun updateContacts(list: List<ContactData>): Flow<Resource<Nothing?>> =
            callbackFlow {
                val phonesReference = reference.child(NODE_PHONE)
                val listener = updater.listener(list) { trySendBlocking(it) }
                phonesReference.addValueEventListener(listener)
                awaitClose { phonesReference.removeEventListener(listener) }
            }
    }
}