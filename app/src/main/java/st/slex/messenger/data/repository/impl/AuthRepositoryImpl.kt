package st.slex.messenger.data.repository.impl

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
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

    override suspend fun signInWithPhone(activity: Activity, phone: String) = callbackFlow {
        val callback = makeCallback({ credential ->
            signInWithCredential(
                credential, {
                    trySendBlocking(AuthResponse.Success)
                }, {
                    trySendBlocking(AuthResponse.Failure(it))
                })
        }, {
            trySendBlocking(AuthResponse.Failure(it))
        }, {
            trySendBlocking(AuthResponse.Send(it))
        })

        val phoneOptions = PhoneAuthOptions
            .newBuilder(Firebase.auth)
            .setActivity(activity)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
        awaitClose {}
    }

    override suspend fun sendCode(id: String, code: String) = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithCredential(credential,
            { trySendBlocking(AuthResponse.Success) },
            { trySendBlocking(AuthResponse.Failure(it)) })
        awaitClose {}
    }

    private inline fun signInWithCredential(
        credential: AuthCredential,
        crossinline success: () -> Unit,
        crossinline failure: (java.lang.Exception) -> Unit
    ) = Firebase.auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success()
            } else task.exception?.let {
                failure(it)
            }
        }

    private inline fun makeCallback(
        crossinline success: (PhoneAuthCredential) -> Unit,
        crossinline failure: (FirebaseException) -> Unit,
        crossinline send: (String) -> Unit
    ) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential): Unit =
                success(credential)

            override fun onVerificationFailed(e: FirebaseException): Unit = failure(e)
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ): Unit = send(verificationId)
        }

    override suspend fun authUser(): Flow<VoidResponse> = callbackFlow {
        val id = Firebase.auth.currentUser?.uid.toString()
        val phone = Firebase.auth.currentUser?.phoneNumber.toString()
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