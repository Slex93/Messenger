package st.slex.messenger.auth.data.core

import com.google.android.gms.tasks.Task
import st.slex.core.Resource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object CoroutinesHandle {

    suspend fun <T> Task<T>.handle(): Resource<T> =
        suspendCoroutine { continuation ->
            addOnSuccessListener { continuation.resume(Resource.Success(it)) }
            addOnFailureListener { continuation.resume(Resource.Failure(it)) }
        }
}