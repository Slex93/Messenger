package st.slex.messenger.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.core.VoidDataResult
import st.slex.messenger.utilites.CHILD_ID
import st.slex.messenger.utilites.CHILD_PHONE
import st.slex.messenger.utilites.NODE_PHONE
import st.slex.messenger.utilites.NODE_USER
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
interface AuthRepository {

    suspend fun saveUser(): Flow<VoidDataResult>

    class Base @Inject constructor(
        private val reference: Lazy<DatabaseReference>,
        private val user: Lazy<FirebaseUser>
    ) : AuthRepository {

        override suspend fun saveUser(): Flow<VoidDataResult> = callbackFlow {
            val result1 = userReference.updateChildren(mapUser)
            val result2 = phoneReference.setValue(user.get().uid)
            makeHandle(result1, result2) { trySendBlocking(it) }
            awaitClose { }
        }

        private suspend inline fun makeHandle(
            result1: Task<Void>,
            result2: Task<Void>,
            crossinline function: (VoidDataResult) -> Unit
        ) = try {
            val handleTask = handle(result1)
            if (handleTask is VoidDataResult.Success) function(handle(result2))
            else function(handleTask)
        } catch (exception: Exception) {
            function(VoidDataResult.Failure(exception))
        }

        private suspend fun handle(result: Task<Void>): VoidDataResult =
            suspendCoroutine { continuation ->
                result.addOnSuccessListener { continuation.resume(VoidDataResult.Success) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }

        private val userReference: DatabaseReference by lazy {
            reference.get().child(NODE_USER).child(user.get().uid)
        }

        private val phoneReference: DatabaseReference by lazy {
            reference.get().child(NODE_PHONE).child(user.get().phoneNumber.toString())
        }

        private val mapUser: Map<String, Any> by lazy {
            mapOf<String, Any>(
                CHILD_ID to user.get().uid,
                CHILD_PHONE to user.get().phoneNumber.toString()
            )
        }

    }
}