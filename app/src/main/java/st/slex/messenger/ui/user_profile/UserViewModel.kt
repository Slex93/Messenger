package st.slex.messenger.ui.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.domain.user.UserDomainMapper
import st.slex.messenger.domain.user.UserDomainResult
import st.slex.messenger.domain.user.UserInteractor
import st.slex.messenger.ui.core.VoidUIResponse
import st.slex.messenger.ui.core.VoidUIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserViewModel
@Inject constructor(
    private val repository: UserRepository,
    private val interactor: UserInteractor,
    private val mapper: UserDomainMapper<UserUiResult>,
    private val response: VoidUIResponse
) : ViewModel() {
    suspend fun currentUser(): StateFlow<UserUiResult> =
        interactor.getCurrentUser().mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserUiResult.Loading
        )

    suspend fun saveUsername(username: String): StateFlow<VoidUIResult> =
        response.create(repository.saveUsername(username)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidUIResult.Loading
        )

    private suspend fun Flow<UserDomainResult>.mapIt(): Flow<UserUiResult> = callbackFlow {
        this@mapIt.collect {
            trySendBlocking(it.map(mapper))
        }
        awaitClose { }
    }
}