package st.slex.messenger.ui.auth

import com.google.firebase.FirebaseException
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
import st.slex.messenger.domain.LoginDomainResult
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface LoginEngine {
    suspend fun login(phone: String): Flow<LoginDomainResult>

    @ExperimentalCoroutinesApi
    class Base @Inject constructor(
        private val activity: AuthActivity
    ) : LoginEngine {

        override suspend fun login(phone: String): Flow<LoginDomainResult> =
            callbackFlow {
                val callback = makeCallback({ credential ->
                    Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            trySendBlocking(LoginDomainResult.Success)
                        } else {
                            trySendBlocking(LoginDomainResult.Failure(task.exception!!))
                        }
                    }

                }, {
                    trySendBlocking(LoginDomainResult.Failure(it))
                }, {
                    trySendBlocking(LoginDomainResult.SendCode(it))
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

        private inline fun makeCallback(
            crossinline verificationCompleted: (PhoneAuthCredential) -> Unit,
            crossinline verificationFailed: (FirebaseException) -> Unit,
            crossinline codeSend: (String) -> Unit
        ) = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential): Unit =
                verificationCompleted(credential)

            override fun onVerificationFailed(e: FirebaseException): Unit = verificationFailed(e)
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ): Unit = codeSend(verificationId)
        }

    }
}