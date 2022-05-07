package st.slex.messenger.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginRepository {

    suspend fun saveUser(user: UserInitial)

    fun user(): Any?

    class Base : LoginRepository {
        override fun user() = Firebase.auth.currentUser
        override suspend fun saveUser(user: UserInitial) {
            val value = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(user()!!.uid)
                .setValue(user)
            handleResult(value)
        }

        private suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { continuation ->
                value.addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
    }
}