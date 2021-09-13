package st.slex.messenger.data.repository.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor(
    private val currentUser: Lazy<FirebaseUser>,
    private val reference: Lazy<DatabaseReference>
) : AuthRepository {

    override suspend fun saveUser(): Flow<VoidResponse> = callbackFlow {
        val usernameReference = reference.get()
            .child(NODE_USER)
            .child(currentUser.get().uid)
            .child(CHILD_USERNAME)
        val listener = AppValueEventListener({ snapshot ->
            val username = if (snapshot.value != null) {
                snapshot.getThisValue<String>()
            } else currentUser.get().uid

            val map = mapOf<String, Any>(
                CHILD_PHONE to currentUser.get().phoneNumber.toString(),
                CHILD_ID to currentUser.get().uid,
                CHILD_USERNAME to username
            )

            val userTask = reference.get()
                .child(NODE_USER)
                .child(currentUser.get().uid)
                .updateChildren(map)

            val phonesTask = reference.get()
                .child(NODE_PHONE)
                .child(currentUser.get().uid)
                .setValue(currentUser.get().phoneNumber.toString())

            val usernameTask = reference.get()
                .child(NODE_USERNAME)
                .child(currentUser.get().uid)
                .setValue(username)

            userTask.listener({
                phonesTask.listener({
                    usernameTask.listener({
                        trySendBlocking(it)
                    }, {
                        trySendBlocking(it)
                    })
                }, {
                    trySendBlocking(it)
                })
            }, {
                trySendBlocking(it)
            })

        }, {
            trySendBlocking(VoidResponse.Failure(it))
        })
        usernameReference.addValueEventListener(listener)
        awaitClose { usernameReference.removeEventListener(listener) }
    }

    private inline fun Task<Void>.listener(
        crossinline success: (VoidResponse.Success) -> Unit,
        crossinline failure: (VoidResponse.Failure) -> Unit
    ) {
        this.addOnCompleteListener {
            if (it.isSuccessful) {
                success(VoidResponse.Success)
            } else failure(VoidResponse.Failure(it.exception!!))
        }
    }


}