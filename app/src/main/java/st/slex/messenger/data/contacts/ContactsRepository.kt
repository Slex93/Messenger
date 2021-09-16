package st.slex.messenger.data.contacts

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.utilites.NODE_CONTACT
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsRepository {
    suspend fun getContacts(): Flow<ContactsDataResult>

    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ContactsRepository {

        override suspend fun getContacts(): Flow<ContactsDataResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_CONTACT)
                .child(user.uid)
            val listener = AppValueEventListener({ snapshot ->
                val result = snapshot.children.mapNotNull {
                    it.getValue(ContactsData.Base::class.java)
                }
                trySendBlocking(ContactsDataResult.Success(result))
            }, {
                trySendBlocking(ContactsDataResult.Failure(it))
            })
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

    }
}

