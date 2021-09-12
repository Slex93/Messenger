package st.slex.messenger.data.repository.impl

import android.util.Log
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
        Log.i("Response::save:", "start")

        val uid = Firebase.auth.currentUser!!.uid
        val map = mapOf<String, Any>(
            CHILD_PHONE to Firebase.auth.currentUser?.phoneNumber.toString(),
            CHILD_ID to uid
        )
        val value = FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(uid)
            .setValue(map)
        value.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i("Response::save:", "Success")
                trySendBlocking(VoidResponse.Success)
            } else {
                Log.i("Response::save:", "Failure")
                trySendBlocking(VoidResponse.Failure(it.exception!!))
            }
        }
        awaitClose {}
    }

}