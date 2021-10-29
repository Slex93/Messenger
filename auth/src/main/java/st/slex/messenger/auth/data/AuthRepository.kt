package st.slex.messenger.auth.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import st.slex.messenger.core.FirebaseConstants.CHILD_ID
import st.slex.messenger.core.FirebaseConstants.CHILD_PHONE
import st.slex.messenger.core.FirebaseConstants.NODE_PHONE
import st.slex.messenger.core.FirebaseConstants.NODE_TOKENS
import st.slex.messenger.core.FirebaseConstants.NODE_USER
import st.slex.messenger.core.Resource
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
interface AuthRepository {

    suspend fun saveUser(): Flow<Resource<Void>>

    class Base @Inject constructor(
        private val reference: Lazy<DatabaseReference>,
        private val user: Lazy<FirebaseUser>
    ) : AuthRepository {

        override suspend fun saveUser(): Flow<Resource<Void>> = flow {
            val updateUserTask = userReference.updateChildren(mapUser)
            val updatePhoneTask = phoneReference.setValue(user.get().uid)
            updateUserTask.handle().also { updateUserResult ->
                if (updateUserResult is Resource.Success) {
                    sendToken().also {
                        if (it is Resource.Failure) emit(Resource.Failure(it.exception))
                    }
                    emit(updatePhoneTask.handle())
                } else emit(updateUserResult)
            }
        }

        private suspend fun sendToken() = withContext(Dispatchers.IO) {
            FirebaseMessaging.getInstance().token.handle().also { token ->
                return@withContext tokenReference.setValue(token).handle()
            }
        }

        private suspend fun <T> Task<T>.handle(): Resource<T> =
            suspendCoroutine { continuation ->
                addOnSuccessListener { continuation.resume(Resource.Success(it)) }
                addOnFailureListener { continuation.resume(Resource.Failure(it)) }
            }

        private val tokenReference: DatabaseReference by lazy {
            reference.get().child(NODE_TOKENS).child(user.get().uid)
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