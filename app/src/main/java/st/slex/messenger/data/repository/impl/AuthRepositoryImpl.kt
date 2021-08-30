package st.slex.messenger.data.repository.impl

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.funs.callback
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.funs.signInWithPhone
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithPhone(phone: String, activity: Activity) = callbackFlow {
        val callback = callback({ credential ->
            auth.signInWithPhone(credential, {
                trySendBlocking(AuthResponse.Success).isSuccess
            }, {
                trySendBlocking(AuthResponse.Failure(it)).isFailure
            })
        }, { exception ->
            trySendBlocking(AuthResponse.Failure(exception)).isFailure
        }, { id, _ ->
            trySendBlocking(AuthResponse.Send(id)).isSuccess
        })
        val phoneOptions = PhoneAuthOptions
            .newBuilder(FirebaseAuth.getInstance())
            .setActivity(activity)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()
        val event = PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
        awaitClose { event }
    }

    override suspend fun sendCode(id: String, code: String) = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(id, code)
        val event = auth.signInWithPhone(credential, {
            trySendBlocking(AuthResponse.Success)
        }, {
            trySendBlocking(AuthResponse.Failure(it))
        })
        awaitClose { event }
    }

    override suspend fun authUser(): Flow<VoidResponse> = callbackFlow {
        val id = auth.currentUser?.uid.toString()
        val phone = auth.currentUser?.phoneNumber.toString()
        val userReference = databaseReference.child(NODE_USER).child(id)
        val listener = AppValueEventListener({ snapshot ->
            val user = snapshot.getThisValue<UserModel>()
            val username = if (user.username.isNullOrEmpty()) {
                id
            } else user.username
            val mapUser = mapOf(
                CHILD_ID to id,
                CHILD_PHONE to phone,
                CHILD_USERNAME to username
            )
            databaseReference.child(NODE_PHONE).child(id)
                .setValue(phone)
                .addOnSuccessListener {
                    databaseReference.child(NODE_USER).child(id).updateChildren(mapUser)
                        .addOnSuccessListener {
                            databaseReference.child(NODE_USERNAME).child(username)
                                .setValue(id).addOnSuccessListener {
                                    trySendBlocking(VoidResponse.Success)
                                }
                        }
                }
        }, { exception ->
            trySendBlocking(VoidResponse.Failure(exception))
        })
        userReference.addListenerForSingleValueEvent(listener)
        awaitClose { userReference.removeEventListener(listener) }
    }

}