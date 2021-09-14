package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.UserRepository
import st.slex.messenger.utilites.CHILD_USERNAME
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.NODE_USERNAME
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val user: FirebaseUser
) : UserRepository {

    override suspend fun getCurrentUser(): Flow<Response<UserModel>> = callbackFlow {
        val reference = databaseReference
            .child(NODE_USER)
            .child(user.uid)
        val listener = AppValueEventListener({ snapshot ->
            trySendBlocking(Response.Success(snapshot.getThisValue()))
        }, {
            trySendBlocking(Response.Failure(it))
        })
        reference.addListenerForSingleValueEvent(listener)
        awaitClose { reference.removeEventListener(listener) }
    }

    override suspend fun saveUsername(username: String): Flow<VoidResponse> = callbackFlow {
        val reference = databaseReference
            .child(NODE_USERNAME)
        val listener = AppValueEventListener({ snapshotUsernames ->
            val listOfUsernames = snapshotUsernames.children.filter {
                it.key != user.uid
            }.map {
                it.value
            }
            if (listOfUsernames.contains(username)) {
                trySendBlocking(VoidResponse.Failure(Exception("Take another username")))
            } else {
                val taskUser = databaseReference
                    .child(NODE_USER)
                    .child(user.uid)
                    .child(CHILD_USERNAME)
                    .setValue(username)
                val taskUsername = databaseReference
                    .child(NODE_USERNAME)
                    .child(user.uid)
                    .setValue(username)

                taskUser.addOnCompleteListener { responseUser ->
                    if (responseUser.isSuccessful) {
                        taskUsername.addOnCompleteListener { responseUsername ->
                            if (responseUsername.isSuccessful) {
                                trySendBlocking(VoidResponse.Success)
                            } else {
                                trySendBlocking(VoidResponse.Failure(responseUsername.exception!!))
                            }
                        }
                    } else {
                        trySendBlocking(VoidResponse.Failure(responseUser.exception!!))
                    }
                }
            }

        }, {
            trySendBlocking(VoidResponse.Failure(it))
        })

        reference.addValueEventListener(listener)

        awaitClose { reference.removeEventListener(listener) }
    }


}