package st.slex.messenger.data.repository.impl

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.CHILD_ID
import st.slex.messenger.utilites.CHILD_PHONE
import st.slex.messenger.utilites.NODE_PHONE
import st.slex.messenger.utilites.NODE_USER
import st.slex.messenger.utilites.result.AuthResult
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
                trySendBlocking(AuthResult.Success).isSuccess
            }, {
                trySendBlocking(AuthResult.Failure(it)).isFailure
            })
        }, { exception ->
            trySendBlocking(AuthResult.Failure(exception)).isFailure
        }, { id, token ->
            trySendBlocking(AuthResult.Send).isSuccess
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

    override suspend fun sendCode(id: String, code: String): Flow<AuthResult> = callbackFlow {
        val credential = PhoneAuthProvider.getCredential(id, code)
        val event = auth.signInWithPhone(credential, {
            AuthResult.Success
        }, {
            AuthResult.Failure(it)
        })
        awaitClose { event }
    }

    override suspend fun authUser(): Unit =
        withContext(Dispatchers.IO) {
            val id = auth.currentUser?.uid.toString()
            val phone = auth.currentUser?.phoneNumber.toString()
            val mapUser = mapOf(
                CHILD_ID to id,
                CHILD_PHONE to phone
            )
            databaseReference.child(NODE_PHONE).child(id)
                .setValue(phone)
                .addOnSuccessListener {
                    databaseReference.child(NODE_USER).child(id)
                        .updateChildren(mapUser)
                        .addOnSuccessListener {
                            AuthResult.Success
                        }
                }
        }

    private inline fun FirebaseAuth.signInWithPhone(
        credential: PhoneAuthCredential,
        crossinline success: () -> Unit,
        crossinline failure: (Exception) -> Unit
    ): Task<com.google.firebase.auth.AuthResult> =
        signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success()
            } else {
                failure(task.exception as Exception)
            }
        }.addOnFailureListener {
            failure(it)
        }


    private inline fun callback(
        crossinline onVerificationCompleted: (credential: PhoneAuthCredential) -> Unit,
        crossinline onVerificationFailed: (p0: FirebaseException) -> Unit,
        crossinline onCodeSent: (id: String, token: PhoneAuthProvider.ForceResendingToken) -> Unit
    ) =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential): Unit =
                onVerificationCompleted(credential)

            override fun onVerificationFailed(p0: FirebaseException): Unit =
                onVerificationFailed(p0)

            override fun onCodeSent(
                id: String,
                token: PhoneAuthProvider.ForceResendingToken
            ): Unit =
                onCodeSent(id, token)
        }


}