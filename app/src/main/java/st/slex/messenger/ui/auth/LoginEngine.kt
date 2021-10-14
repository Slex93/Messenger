package st.slex.messenger.ui.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.domain.LoginDomainResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface LoginEngine {
    suspend fun login(phone: String): LoginDomainResult

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val activity: AuthActivity
    ) : LoginEngine {

        override suspend fun login(phone: String): LoginDomainResult =
            suspendCoroutine { continuation ->
                val callback = callback { continuation.resumeWith(Result.success(it)) }
                val phoneOptions = PhoneAuthOptions
                    .newBuilder(Firebase.auth)
                    .setActivity(activity)
                    .setPhoneNumber(phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(callback)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(phoneOptions)
            }

        private inline fun callback(
            crossinline function: (LoginDomainResult) -> Unit
        ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        function(LoginDomainResult.Success.LogIn)
                    } else {
                        function(LoginDomainResult.Failure(task.exception!!))
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException): Unit =
                function(LoginDomainResult.Failure(e))

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ): Unit = function(LoginDomainResult.Success.SendCode(verificationId))
        }
    }
}