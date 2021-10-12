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
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.utilites.NODE_CONTACT
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ContactsRepository {
    suspend fun getContacts(): Flow<DataResult<List<ContactsData>>>

    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : ContactsRepository {

        override suspend fun getContacts(): Flow<DataResult<List<ContactsData>>> = callbackFlow {
            val reference = databaseReference
                .child(NODE_CONTACT)
                .child(user.uid)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.children.mapNotNull {
                        it.getValue(ContactsData.Base::class.java)
                    }
                    trySendBlocking(DataResult.Success(result))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySendBlocking(DataResult.Failure(error.toException()))
                }

            }
            reference.addValueEventListener(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        //TODO
        inline fun <reified D> listener(
            crossinline function: (DataResult<D>) -> Unit
        ): ValueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(D::class.java)!!
                function(DataResult.Success(result))
            }

            override fun onCancelled(error: DatabaseError) {
                function(DataResult.Failure(error.toException()))
            }

        }

    }
}

