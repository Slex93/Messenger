package st.slex.messenger.auth.ui

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.messenger.auth.core.LoginValue
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface LoginEngine {
    suspend fun login(phone: String): LoginValue

    class Base @Inject constructor(
        private val activity: AuthActivity
    ) : LoginEngine {

        override suspend fun login(phone: String): LoginValue =
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
            crossinline function: (LoginValue) -> Unit
        ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        function(LoginValue.Success.LogIn)
                    } else {
                        function(LoginValue.Failure(task.exception!!))
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException): Unit =
                function(LoginValue.Failure(e))

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ): Unit = function(LoginValue.Success.SendCode(verificationId))
        }
    }
}