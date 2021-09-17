package st.slex.messenger.domain.user

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.data.profile.UserRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface UserInteractor {
    suspend fun getCurrentUser(): Flow<UserDomainResult>
    suspend fun getUser(uid: String): Flow<UserDomainResult>
    class Base @Inject constructor(
        private val repository: UserRepository,
        private val mapper: UserDataMapper<UserDomainResult>,
        private val user: FirebaseUser
    ) : UserInteractor {
        override suspend fun getCurrentUser(): Flow<UserDomainResult> = callbackFlow {
            repository.getUser(user.uid).collect {
                trySendBlocking(it.map(mapper))
            }
            awaitClose { }
        }

        override suspend fun getUser(uid: String): Flow<UserDomainResult> = callbackFlow {
            repository.getUser(uid).collect {
                trySendBlocking(it.map(mapper))
            }
            awaitClose { }
        }


    }
}