package st.slex.messenger.data.profile

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.core.AppValueEventListener
import st.slex.messenger.core.VoidResult
import st.slex.messenger.utilites.CHILD_USERNAME
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.NODE_USERNAME
import javax.inject.Inject

interface UserRepository {
    suspend fun getUser(uid: String): Flow<UserDataResult>
    suspend fun saveUsername(username: String): Flow<VoidResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val databaseReference: DatabaseReference,
        private val user: FirebaseUser
    ) : UserRepository {

        override suspend fun getUser(uid: String): Flow<UserDataResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USER)
                .child(uid)
            val listener = AppValueEventListener({ snapshot ->
                trySendBlocking(
                    UserDataResult.Success(snapshot.getValue(UserData.Base::class.java)!!)
                )
            }, {
                trySendBlocking(UserDataResult.Failure(it))
            })
            reference.addListenerForSingleValueEvent(listener)
            awaitClose { reference.removeEventListener(listener) }
        }

        override suspend fun saveUsername(username: String): Flow<VoidResult> = callbackFlow {
            val reference = databaseReference
                .child(NODE_USERNAME)
            val listener = AppValueEventListener({ snapshotUsernames ->
                val listOfUsernames = snapshotUsernames.children.filter {
                    it.key != user.uid
                }.map {
                    it.value
                }
                if (listOfUsernames.contains(username)) {
                    trySendBlocking(VoidResult.Failure(Exception("Take another username")))
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
                                    trySendBlocking(VoidResult.Success)
                                } else {
                                    trySendBlocking(VoidResult.Failure(responseUsername.exception!!))
                                }
                            }
                        } else {
                            trySendBlocking(VoidResult.Failure(responseUser.exception!!))
                        }
                    }
                }

            }, {
                trySendBlocking(VoidResult.Failure(it))
            })

            reference.addValueEventListener(listener)

            awaitClose { reference.removeEventListener(listener) }
        }


    }
}