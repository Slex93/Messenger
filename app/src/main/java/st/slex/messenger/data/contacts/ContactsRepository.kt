package st.slex.messenger.data.contacts

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.Resource
import st.slex.messenger.utilites.NODE_CONTACT
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsRepository {

    suspend fun getContacts(): Flow<Resource<List<ContactsData>>>

    class Base @Inject constructor(
        private val reference: DatabaseReference,
        private val user: FirebaseUser
    ) : ContactsRepository {

        override suspend fun getContacts(): Flow<Resource<List<ContactsData>>> =
            callbackFlow {
                val listener = listener { trySendBlocking(it) }
                contactsReference.addListenerForSingleValueEvent(listener)
                awaitClose { contactsReference.removeEventListener(listener) }
            }

        private val contactsReference: DatabaseReference by lazy {
            reference.child(NODE_USER).child(user.uid).child(NODE_CONTACT)
        }

        private inline fun listener(
            crossinline function: (Resource<List<ContactsData>>) -> Unit
        ): ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.children.mapNotNull {
                    it.getValue(ContactsData.Base::class.java)!!
                }
                function(Resource.Success(result))
            }

            override fun onCancelled(error: DatabaseError) {
                function(Resource.Failure(error.toException()))
            }
        }
    }
}

