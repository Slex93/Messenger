package st.slex.messenger.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import st.slex.messenger.core.Resource
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

    suspend fun saveUser(): Flow<Resource<Nothing?>>

    class Base @Inject constructor(
        private val reference: Lazy<DatabaseReference>,
        private val user: Lazy<FirebaseUser>
    ) : AuthRepository {

        override suspend fun saveUser(): Flow<Resource<Nothing?>> = flow {
            val task1 = userReference.updateChildren(mapUser)
            val task2 = phoneReference.setValue(user.get().uid)
            handle(task1).also {
                if (it is Resource.Success) emit(handle(task2))
                else emit(it)
            }
        }

        private suspend fun handle(task: Task<Void>): Resource<Nothing?> =
            suspendCoroutine { continuation ->
                task.addOnSuccessListener { continuation.resume(Resource.Success(null)) }
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