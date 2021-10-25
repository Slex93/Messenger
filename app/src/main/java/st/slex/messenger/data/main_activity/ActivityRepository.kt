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
import st.slex.messenger.core.FirebaseConstants.CHILD_FULL_NAME
import st.slex.messenger.core.FirebaseConstants.CHILD_ID
import st.slex.messenger.core.FirebaseConstants.CHILD_PHONE
import st.slex.messenger.core.FirebaseConstants.CHILD_STATE
import st.slex.messenger.core.FirebaseConstants.NODE_CONTACT
import st.slex.messenger.core.FirebaseConstants.NODE_PHONE
import st.slex.messenger.core.FirebaseConstants.NODE_USER
import st.slex.messenger.core.Resource
import st.slex.messenger.data.contacts.ContactData
import st.slex.messenger.utilites.*
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
    ) : ActivityRepository {

        override suspend fun changeState(state: String): Unit = suspendCoroutine { continuation ->
            val task = stateReference.setValue(state)
            task.addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }

        override suspend fun updateContacts(list: List<ContactData>): Flow<Resource<Nothing?>> =
            callbackFlow {
                val phoneListener = listener(list) {
                    trySendBlocking(it)
                }
                phonesNumberReference.addValueEventListener(phoneListener)
                awaitClose { phonesNumberReference.removeEventListener(phoneListener) }
            }

        private suspend inline fun listener(
            contactsList: List<ContactData>,
            crossinline function: (Resource<Nothing?>) -> Unit
        ) = withContext(Dispatchers.IO) {
            return@withContext object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    contactsParser(contactsList, snapshot.mapIdToPhones).forEach {
                        val task = contactsReference.child(it.getId).setValue(mapContact(it))
                        this@withContext.launch {
                            function(handle(task))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    function(Resource.Failure(error.toException()))
                }
            }
        }

        private suspend fun handle(result: Task<Void>): Resource<Nothing?> =
            suspendCoroutine { continuation ->
                result.addOnSuccessListener { continuation.resume(Resource.Success(null)) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }

        private fun contactsParser(
            contacts: List<ContactData>,
            snapUsers: Map<String, String>
        ): List<ContactData> =
            contacts.filter { contact ->
                snapUsers.containsKey(contact.getPhone) && !snapUsers.containsValue(auth.uid)
            }.map { contact ->
                contact.copy(id = snapUsers[contact.getPhone])
            }

        private val DataSnapshot.mapIdToPhones: Map<String, String>
            get() = children.mapNotNull {
                it.key.toString() to it.value.toString()
            }.toMap()

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(auth.uid).child(NODE_CONTACT)
        }

        private val phonesNumberReference: DatabaseReference by lazy {
            reference.child(NODE_PHONE)
        }

        private val stateReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(auth.uid).child(CHILD_STATE)
        }

        private fun mapContact(contact: ContactData) = mapOf<String, Any>(
            CHILD_ID to contact.getId,
            CHILD_PHONE to contact.getPhone,
            CHILD_FULL_NAME to contact.getFullName
        )
    }
}