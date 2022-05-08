package st.slex.messenger.auth.ui.utils

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.messenger.auth.core.LoginValue

class PhoneAuthCallback(
    private val function: (LoginValue) -> Unit
) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                function(LoginValue.Success.LogIn)
            } else {
                function(LoginValue.Failure(task.exception!!))
            }
        }
    }

    override fun onVerificationFailed(exception: FirebaseException) {
        function(LoginValue.Failure(exception))
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        function(LoginValue.Success.SendCode(verificationId))
    }
}