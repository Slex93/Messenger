package st.slex.messenger.auth.ui

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.auth.domain.LoginDomainResult
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface SendCodeEngine {

    suspend fun sendCode(id: String, code: String): LoginDomainResult

    @ExperimentalCoroutinesApi
    class Base @Inject constructor() : SendCodeEngine {

        override suspend fun sendCode(id: String, code: String): LoginDomainResult =
            suspendCoroutine { continuation ->
                val credential = PhoneAuthProvider.getCredential(id, code)
                val task = Firebase.auth.signInWithCredential(credential)
                val listener = listener { continuation.resumeWith(Result.success(it)) }
                task.addOnCompleteListener(listener)
            }

        private inline fun listener(
            crossinline function: (LoginDomainResult) -> Unit
        ) = OnCompleteListener<AuthResult> {
            if (it.isSuccessful) function(LoginDomainResult.Success.LogIn)
            else function(LoginDomainResult.Failure(it.exception!!))
        }
    }
}