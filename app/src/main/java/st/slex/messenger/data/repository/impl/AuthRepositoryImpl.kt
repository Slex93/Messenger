package st.slex.messenger.data.repository.impl

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.CHILD_ID
import st.slex.messenger.utilites.CHILD_PHONE
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun saveUser(): Flow<VoidResponse> = callbackFlow {
        val currentUser = Firebase.auth.currentUser!!
        val map = mapOf<String, Any>(
            CHILD_PHONE to currentUser.phoneNumber.toString(),
            CHILD_ID to currentUser.uid
        )
        val value = FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(currentUser.uid)
            .updateChildren(map)
        value.addOnCompleteListener {
            if (it.isSuccessful) {
                trySendBlocking(VoidResponse.Success)
            } else {
                trySendBlocking(VoidResponse.Failure(it.exception!!))
            }
        }
        awaitClose {}
    }
}