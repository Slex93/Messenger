package st.slex.messenger.data.repository.impl

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.*
import st.slex.messenger.utilites.base.AppValueEventListener
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@ExperimentalCoroutinesApi
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override suspend fun signInWithPhone(phone: String, activity: Activity) = callbackFlow {

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithCredential:success")
                            trySendBlocking(AuthResponse.Success)
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            trySendBlocking(AuthResponse.Failure(task.exception as Exception))
                        }
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                trySendBlocking(AuthResponse.Failure(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                trySendBlocking(AuthResponse.Send(verificationId))
            }
        }

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
        val event = FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySendBlocking(AuthResponse.Success)
                } else {
                    trySendBlocking(AuthResponse.Failure(task.exception as Exception))
                }
            }
        awaitClose { event }
    }


    override suspend fun authUser(): Flow<VoidResponse> = callbackFlow {
        val id = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val phone = FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
        val userReference = FirebaseDatabase.getInstance().reference.child(NODE_USER).child(id)
        val listener = AppValueEventListener({ snapshot ->
            val user = snapshot.getThisValue<UserModel>()
            val username = if (user.username.isEmpty()) {
                id
            } else user.username
            val mapUser = mapOf(
                CHILD_ID to id,
                CHILD_PHONE to phone,
                CHILD_USERNAME to username
            )
            FirebaseDatabase.getInstance().reference.child(NODE_PHONE).child(id)
                .setValue(phone)
                .addOnSuccessListener {
                    FirebaseDatabase.getInstance().reference.child(NODE_USER).child(id)
                        .updateChildren(mapUser)
                        .addOnSuccessListener {
                            FirebaseDatabase.getInstance().reference.child(NODE_USERNAME)
                                .child(username)
                                .setValue(id).addOnSuccessListener {
                                    trySendBlocking(VoidResponse.Success)
                                }.addOnCanceledListener {
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