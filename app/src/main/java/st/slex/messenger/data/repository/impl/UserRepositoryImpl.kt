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
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
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
}