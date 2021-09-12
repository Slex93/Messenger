package st.slex.messenger.data.repository.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import st.slex.messenger.data.model.UserInitial
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    override val user: FirebaseUser?
        get() = Firebase.auth.currentUser

    override suspend fun saveUser(user: UserInitial) {
        val value = FirebaseDatabase.getInstance().reference
            .child(NODE_USER)
            .child(this.user!!.uid)
            .setValue(user)
        handleResult(value)
    }

    private suspend fun handleResult(value: Task<Void>): Unit = suspendCoroutine { continuation ->
        value.addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }
}