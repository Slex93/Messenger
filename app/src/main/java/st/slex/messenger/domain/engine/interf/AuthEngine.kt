package st.slex.messenger.domain.engine.interf

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface AuthEngine

inline fun AuthEngine.signInWithCredential(
    credential: AuthCredential,
    crossinline success: () -> Unit,
    crossinline failure: (java.lang.Exception) -> Unit
) = Firebase.auth.signInWithCredential(credential)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            success()
        } else task.exception?.let {
            failure(it)
        }
    }