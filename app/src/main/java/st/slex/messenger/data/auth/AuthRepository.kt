package st.slex.messenger.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.slex.messenger.data.core.DataResult
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

    suspend fun saveUser(): Flow<DataResult<*>>

    class Base @Inject constructor(
        private val reference: Lazy<DatabaseReference>,
        private val user: Lazy<FirebaseUser>
    ) : AuthRepository {

        override suspend fun saveUser(): Flow<DataResult<*>> = flow {
            val result1 = userReference.updateChildren(mapUser)
            val result2 = phoneReference.setValue(user.get().uid)
            val handleTask = handle(result1)
            if (handleTask is DataResult.Success) emit(handle(result2))
            else emit(handleTask)
        }

        private suspend fun handle(result: Task<Void>): DataResult<*> =
            suspendCoroutine { continuation ->
                result.addOnSuccessListener { continuation.resume(DataResult.Success(null)) }
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