package st.slex.messenger.data.repository.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun saveUser(): Flow<VoidResponse> = callbackFlow {
        val currentUser = Firebase.auth.currentUser!!
        val map = mapOf<String, Any>(
            CHILD_PHONE to currentUser.phoneNumber.toString(),
            CHILD_ID to currentUser.uid,
            CHILD_USERNAME to currentUser.displayName.toString()
        )
        val user = FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(currentUser.uid)
            .updateChildren(map)
        val phones = FirebaseDatabase.getInstance().reference
            .child(NODE_PHONE)
            .child(currentUser.uid)
            .setValue(currentUser.phoneNumber.toString())
        val username = FirebaseDatabase.getInstance().reference
            .child(NODE_USERNAME)
            .child(currentUser.uid)
            .setValue(currentUser.displayName)

        user.listener { userResponse ->
            if (userResponse is VoidResponse.Success) {
                phones.listener { phonesResponse ->
                    if (phonesResponse is VoidResponse.Success) {
                        username.listener {
                            trySendBlocking(it)
                        }
                    } else trySendBlocking(phonesResponse)
                }
            } else {
                trySendBlocking(userResponse)
            }
        }
        awaitClose {}
    }

    private inline fun Task<Void>.listener(
        crossinline request: (VoidResponse) -> Unit
    ) {
        addOnCompleteListener {
            if (isSuccessful) {
                request(VoidResponse.Success)
            } else {
                request(VoidResponse.Failure(it.exception!!))
            }
        }
    }


}