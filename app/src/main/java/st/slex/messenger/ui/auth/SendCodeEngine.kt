package st.slex.messenger.ui.auth

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.domain.auth.LoginDomainResult
import javax.inject.Inject

interface SendCodeEngine {
    suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor() : SendCodeEngine {
        override suspend fun sendCode(id: String, code: String): Flow<LoginDomainResult> =
            callbackFlow {
                val credential = PhoneAuthProvider.getCredential(id, code)
                signInWithCredential(credential,
                    { trySendBlocking(LoginDomainResult.Success) },
                    { trySendBlocking(LoginDomainResult.Failure(it)) })
                awaitClose {}
            }

        private inline fun signInWithCredential(
            credential: PhoneAuthCredential,
            crossinline success: () -> Unit,
            crossinline failure: (Exception) -> Unit
        ) = Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                success()
            } else {
                failure(it.exception!!)
            }
        }
    }
}