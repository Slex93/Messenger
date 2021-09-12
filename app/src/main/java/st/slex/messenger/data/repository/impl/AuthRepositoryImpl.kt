package st.slex.messenger.data.repository.impl

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.model.UserInitial
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.CHILD_ID
import st.slex.messenger.utilites.CHILD_PHONE
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override suspend fun saveUser(user: UserInitial): Flow<Result<String>> = callbackFlow {
        val map = mapOf<String, Any>(
            CHILD_PHONE to user.phone,
            CHILD_ID to user.uid
        )
        val value = FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(user.uid)
            .setValue(map)
        value.addOnCompleteListener {
            if (it.isSuccessful) {
                trySendBlocking(Result.success(it.result.toString()))
            } else {
                trySendBlocking(Result.failure(it.exception!!))
            }
        }
        awaitClose {}
    }

}