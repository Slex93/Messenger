package st.slex.messenger.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.utilites.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface AuthRepository {

    suspend fun saveUser(user: AuthData): Flow<AuthDataResult>

    class Base @Inject constructor(
        private val reference: Lazy<DatabaseReference>
    ) : AuthRepository {

        override suspend fun saveUser(user: AuthData): Flow<AuthDataResult> = callbackFlow {
            val id = user.id()
            val phone = user.phone()
            val listenReference = userForNameReference(id)
            val listener = userListener(id, phone) {
                trySendBlocking(it)
            }
            listenReference.addValueEventListener(listener)
            awaitClose { listenReference.removeEventListener(listener) }
        }

        private inline fun userListener(
            id: String,
            phone: String,
            crossinline function: (AuthDataResult) -> Unit
        ) = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username: String = (snapshot.value ?: id) as String
                val userMap = userMap(id, phone, username)
                userReference(id, userMap).listen({
                    phonesReference(id, phone).listen({
                        usernameReference(id, username).listen({
                            function(AuthDataResult.Success)
                        }, {
                            AuthDataResult.Failure(it)
                        })
                    }, {
                        function(AuthDataResult.Failure(it))
                    })
                }, {
                    function(AuthDataResult.Failure(it))
                })
            }

            override fun onCancelled(error: DatabaseError) {
                function(AuthDataResult.Failure(error.toException()))
            }
        }

        private inline fun Task<Void>.listen(
            crossinline success: () -> Unit,
            crossinline exception: (Exception) -> Unit
        ) {
            this.addOnCompleteListener {
                if (it.isSuccessful) {
                    success()
                } else exception(it.exception!!)
            }
        }


        private fun userForNameReference(id: String) = reference.get()
            .child(NODE_USER)
            .child(id)
            .child(CHILD_USERNAME)

        private fun userMap(id: String, phone: String, username: String) = mapOf<String, Any>(
            CHILD_PHONE to phone,
            CHILD_ID to id,
            CHILD_USERNAME to username
        )

        private fun userReference(id: String, map: Map<String, Any>) = reference.get()
            .child(NODE_USER)
            .child(id)
            .updateChildren(map)

        private fun phonesReference(id: String, phone: String) = reference.get()
            .child(NODE_PHONE)
            .child(id)
            .setValue(phone)

        private fun usernameReference(id: String, phone: String) = reference.get()
            .child(NODE_USERNAME)
            .child(id)
            .setValue(phone)

    }
}