package st.slex.messenger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import st.slex.messenger.core.Resource
import st.slex.messenger.data.chat.SingleChatRepository
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleChatViewModel @Inject constructor(
    private val repository: SingleChatRepository,
    private val userRepository: UserRepository,
    private val mapper: UserDataMapper
) : ViewModel() {

    suspend fun getUser(uid: String): StateFlow<Resource<UserUI>> =
        userRepository.getUser(uid)
            .flatMapLatest { flowOf(it.map(mapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading()
            )

    private suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        userRepository.getCurrentUser()
            .flatMapLatest { flowOf(it.map(mapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading()
            )

    fun sendMessage(message: String, user: UserUI) = viewModelScope.launch(Dispatchers.IO) {
        currentUser().collect {
            if (it is Resource.Success)
                it.data?.let { current ->
                    repository.sendMessage(message = message, user = user, current).collect()
                }
        }
    }

}