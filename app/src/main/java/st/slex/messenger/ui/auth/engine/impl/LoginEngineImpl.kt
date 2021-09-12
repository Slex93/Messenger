package st.slex.messenger.ui.auth.engine.impl

import android.app.Activity
import android.util.Log
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
import st.slex.messenger.ui.auth.engine.interf.LoginEngine
import st.slex.messenger.utilites.result.AuthResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginEngineImpl @Inject constructor() : LoginEngine {

    override suspend fun login(phone: String, activity: Activity): Flow<AuthResponse> =
        callbackFlow {
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
                Log.i("checkId::Login", it)
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