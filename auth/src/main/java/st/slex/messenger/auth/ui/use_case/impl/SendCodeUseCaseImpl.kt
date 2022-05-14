package st.slex.messenger.auth.ui.use_case.impl

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.messenger.auth.core.LoginValue
import st.slex.messenger.auth.ui.use_case.interf.SendCodeUseCase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class SendCodeUseCaseImpl @Inject constructor() : SendCodeUseCase {

    override suspend fun sendCode(id: String, code: String): LoginValue =
        suspendCoroutine { continuation ->
            val credential = PhoneAuthProvider.getCredential(id, code)
            val task = Firebase.auth.signInWithCredential(credential)
            val listener = listener { continuation.resumeWith(Result.success(it)) }
            task.addOnCompleteListener(listener)
        }

    private inline fun listener(
        crossinline function: (LoginValue) -> Unit
    ) = OnCompleteListener<AuthResult> {
        val authResult = if (it.isSuccessful) LoginValue.Success.LogIn
        else LoginValue.Failure(it.exception!!)
        function(authResult)
    }
}