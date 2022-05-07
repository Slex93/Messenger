package st.slex.messenger.ui.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


interface SendCodeEngine {

    suspend fun sendCode(id: String, code: String): Flow<Auth>

    abstract class Abstract : SendCodeEngine {

        protected suspend fun authResult(pending: Task<AuthResult>) =
            suspendCoroutine<AuthResult> { continuation ->
                pending
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }

    class SendCode : Abstract() {

        override suspend fun sendCode(id: String, code: String): Flow<Auth> = callbackFlow {
            val credential = PhoneAuthProvider.getCredential(id, code)
            val authResult = authInternal(credential)
            trySendBlocking(
                Auth.Base(
                    authResult.additionalUserInfo
                        ?.profile ?: emptyMap()
                )
            )
            awaitClose { }
        }

        private suspend fun authInternal(credential: PhoneAuthCredential) =
            authResult(Firebase.auth.signInWithCredential(credential))
    }
}