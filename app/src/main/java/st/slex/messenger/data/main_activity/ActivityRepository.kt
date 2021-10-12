package st.slex.messenger.data.main_activity

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
import kotlinx.coroutines.withContext
import st.slex.messenger.data.contacts.ContactModel
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.utilites.*
import javax.inject.Inject

interface ActivityRepository {
    suspend fun changeState(state: String)
    suspend fun updateContacts(list: List<ContactModel>): Flow<DataResult<*>>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val auth: FirebaseUser,
    ) : ActivityRepository {

        override suspend fun changeState(state: String): Unit = withContext(Dispatchers.IO) {
            reference.child(NODE_USER).child(auth.uid)
                .child(CHILD_STATE).setValue(state)
        }

        override suspend fun updateContacts(list: List<ContactModel>): Flow<DataResult<*>> =
            callbackFlow {
                val phonesReference = reference.child(NODE_PHONE)

                val listener = listenerPhone({ snapshotListPhone ->
                    snapshotListPhone.children.forEach { snapshotPhone ->
                        if (list.isNullOrEmpty()) {
                            reference.child(NODE_CONTACT).child(auth.uid).setValue(list)
                        } else {
                            list.forEach { contact ->
                                if (auth.uid != snapshotPhone.key && contact.phone == snapshotPhone.value) {
                                    reference
                                        .child(NODE_USER)
                                        .child(snapshotPhone.key.toString())
                                        .child(CHILD_URL)
                                        .addValueEventListener(
                                            listenerPhone({ snapshotUrl ->
                                                val url = snapshotUrl.value
                                                val map = mapOf(
                                                    CHILD_ID to snapshotPhone.key.toString(),
                                                    CHILD_PHONE to contact.phone,
                                                    CHILD_FULL_NAME to contact.full_name,
                                                    CHILD_URL to url
                                                )
                                                val contactTask = reference
                                                    .child(NODE_CONTACT)
                                                    .child(auth.uid)
                                                    .child(snapshotPhone.key.toString())
                                                    .setValue(map)
                                                contactTask.addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        trySendBlocking(DataResult.Success(null))
                                                    } else {
                                                        trySendBlocking(
                                                            DataResult.Failure<Nothing>(
                                                                it.exception!!
                                                            )
                                                        )
                                                    }
                                                }
                                            }, {
                                                trySendBlocking(DataResult.Failure<Nothing>(it))
                                            })
                                        )
                                }
                            }
                        }
                    }
                }, {
                    trySendBlocking(DataResult.Failure<Nothing>(it))
                })

                phonesReference.addValueEventListener(listener)
                awaitClose { phonesReference.removeEventListener(listener) }
            }

        private inline fun listenerPhone(
            crossinline success: (DataSnapshot) -> Unit,
            crossinline error: (Exception) -> Unit
        ) = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) = success(snapshot)
            override fun onCancelled(error: DatabaseError) = error(error.toException())
        }


    }
}