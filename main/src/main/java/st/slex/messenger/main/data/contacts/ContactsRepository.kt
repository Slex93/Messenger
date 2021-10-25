package st.slex.messenger.main.data.contacts

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.FirebaseConstants.CHILD_FULL_NAME
import st.slex.messenger.core.FirebaseConstants.NODE_CONTACT
import st.slex.messenger.core.FirebaseConstants.NODE_USER
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.core.ValueSnapshotListener
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsRepository {

    suspend fun getContactFullName(uid: String): Flow<Resource<String>>
    suspend fun getContacts(): Flow<Resource<List<ContactData>>>
    suspend fun getContact(uid: String): Flow<Resource<ContactData>>

    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val user: FirebaseUser,
        private val listener: ValueSnapshotListener
    ) : ContactsRepository {

        override suspend fun getContactFullName(uid: String): Flow<Resource<String>> =
            callbackFlow {
                val currentReference = contactsReference.child(uid).child(CHILD_FULL_NAME)
                val listener = listener.singleEventListener(String::class) {
                    trySendBlocking(it)
                }
                currentReference.addListenerForSingleValueEvent(listener)
                awaitClose { currentReference.removeEventListener(listener) }
            }

        override suspend fun getContact(uid: String): Flow<Resource<ContactData>> = callbackFlow {
            val listener = listener.singleEventListener(ContactData.Base::class) {
                trySendBlocking(it)
            }
            contactsReference.child(uid).addListenerForSingleValueEvent(listener)
            awaitClose { contactsReference.child(uid).removeEventListener(listener) }
        }

        override suspend fun getContacts(): Flow<Resource<List<ContactData>>> = callbackFlow {
            val listener = listener.multipleEventListener(ContactData.Base::class) {
                trySendBlocking(it)
            }
            contactsReference.addListenerForSingleValueEvent(listener)
            awaitClose { contactsReference.removeEventListener(listener) }
        }

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(user.uid).child(NODE_CONTACT)
        }
    }
}

