package st.slex.messenger.data.contacts

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.Resource
import st.slex.messenger.data.core.Listeners.multipleListener
import st.slex.messenger.data.core.Listeners.singleListener
import st.slex.messenger.utilites.CHILD_FULL_NAME
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsRepository {

    suspend fun getContactFullName(uid: String): Flow<Resource<String>>
    suspend fun getContacts(): Flow<Resource<List<ContactData>>>
    suspend fun getContact(uid: String): Flow<Resource<ContactData>>

    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val user: FirebaseUser
    ) : ContactsRepository {

        override suspend fun getContactFullName(uid: String): Flow<Resource<String>> =
            callbackFlow {
                val listener = singleListener<String> { trySendBlocking(it) }
                val currentReference = contactsReference.child(uid).child(CHILD_FULL_NAME)
                currentReference.addListenerForSingleValueEvent(listener)
                awaitClose { currentReference.removeEventListener(listener) }
            }

        override suspend fun getContact(uid: String): Flow<Resource<ContactData>> = callbackFlow {
            val listener = singleListener<ContactData.Base> { trySendBlocking(it) }
            contactsReference.child(uid).addListenerForSingleValueEvent(listener)
            awaitClose { contactsReference.child(uid).removeEventListener(listener) }
        }

        override suspend fun getContacts(): Flow<Resource<List<ContactData>>> =
            callbackFlow {
                val listener = multipleListener<ContactData.Base> { trySendBlocking(it) }
                contactsReference.addListenerForSingleValueEvent(listener)
                awaitClose { contactsReference.removeEventListener(listener) }
            }

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(user.uid).child(NODE_CONTACT)
        }
    }
}

