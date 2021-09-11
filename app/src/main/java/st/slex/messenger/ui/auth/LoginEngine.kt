package st.slex.messenger.ui.auth

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
interface LoginEngine {

    suspend fun login(phone: String): Flow<Auth>

    abstract class Abstract(activity: Activity) : LoginEngine {

        protected val activityWeakReference = WeakReference(activity)
        protected val phoneAuthBuilder = PhoneAuthOptions.newBuilder(Firebase.auth)

        protected inline fun makeCallback(
            crossinline verificationCompleted: (PhoneAuthCredential) -> Unit,
            crossinline verificationFailed: (FirebaseException) -> Unit,
            crossinline codeSend: (String, PhoneAuthProvider.ForceResendingToken) -> Unit
        ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                verificationCompleted(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                verificationFailed(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                codeSend(verificationId, token)
            }
        }

        protected suspend fun authResult(pending: Task<AuthResult>) =
            suspendCoroutine<AuthResult> { continuation ->
                pending
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }

    class Login(activity: AuthActivity) : Abstract(activity) {

        override suspend fun login(phone: String): Flow<Auth> = callbackFlow {
            val callback = makeCallback({ credential ->
                launch {
                    trySendBlocking(
                        Auth.Base(
                            authInternal(credential)
                                .additionalUserInfo
                                ?.profile ?: emptyMap()
                        )
                    )
                }
            }, {
                trySendBlocking(
                    Auth.Fail(it)
                )
            }, { code, _ ->
                trySendBlocking(
                    Auth.SendCode(code)
                )
            })
            val phoneAuthOptions = generatePhoneAuthOptions(callback, phone)
            verifyPhoneNumber(phoneAuthOptions)
            awaitClose { }
        }

        private fun generatePhoneAuthOptions(
            callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
            phone: String
        ): PhoneAuthOptions = phoneAuthBuilder
            .setActivity(activityWeakReference.get()!!)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callback)
            .build()

        private fun verifyPhoneNumber(phoneAuthOptions: PhoneAuthOptions) =
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

        private suspend fun authInternal(credential: PhoneAuthCredential) =
            authResult(Firebase.auth.signInWithCredential(credential))
    }

}