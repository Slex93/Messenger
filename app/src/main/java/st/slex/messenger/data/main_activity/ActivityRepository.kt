package st.slex.messenger.data.main_activity

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import st.slex.messenger.data.contacts.FirebaseContactModel
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.utilites.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface ActivityRepository {
    suspend fun changeState(state: String)
    suspend fun updateContacts(list: List<FirebaseContactModel>): Flow<DataResult<*>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser,
    ) : ActivityRepository {

        override suspend fun changeState(state: String): Unit = suspendCoroutine { continuation ->
            val task = stateReference.setValue(state)
            task.addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

        override suspend fun updateContacts(
            list: List<FirebaseContactModel>
        ): Flow<DataResult<*>> = callbackFlow {
            val phoneListener = listener(list) {
                trySendBlocking(it)
            }
            phonesNumberReference.addValueEventListener(phoneListener)
            awaitClose { phonesNumberReference.removeEventListener(phoneListener) }
        }

        private suspend inline fun listener(
            list: List<FirebaseContactModel>,
            crossinline function: (DataResult<*>) -> Unit
        ) = withContext(Dispatchers.IO) {
            return@withContext object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { snapshotPhone ->
                        val phone: String = snapshotPhone.key.toString()
                        val id: String = snapshotPhone.value.toString()
                        if (list.isNullOrEmpty()) {
                            this@withContext.launch {
                                function(handle(contactsReference.setValue(list)))
                            }
                        } else {
                            list.forEach { contact ->
                                if (auth.uid != id && contact.phone == phone) {
                                    val task = contactsReference.child(phone).setValue(
                                        mapContact(
                                            id = id,
                                            phone = phone,
                                            username = contact.username ?: ""
                                        )
                                    )
                                    this@withContext.launch {
                                        function(handle(task))
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    function(DataResult.Failure<Nothing>(error.toException()))
                }
            }
        }

        private suspend fun handle(result: Task<Void>): DataResult<*> =
            suspendCoroutine { continuation ->
                result.addOnSuccessListener { continuation.resume(DataResult.Success(null)) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(auth.uid).child(NODE_CONTACT)
        }

        private val phonesNumberReference: DatabaseReference by lazy {
            reference.child(NODE_PHONE)
        }

        private val stateReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(auth.uid).child(CHILD_STATE)
        }

        private fun mapContact(id: String, phone: String, username: String) = mapOf<String, Any>(
            CHILD_ID to id,
            CHILD_PHONE to phone,
            CHILD_FULL_NAME to username
        )
    }
}