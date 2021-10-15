package st.slex.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.chats.ChatsDataMapper
import st.slex.messenger.data.chats.ChatsRepository
import st.slex.messenger.data.profile.UserDataMapper
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatsRepository: ChatsRepository,
    private val chatsMapper: ChatsDataMapper,
    private val userMapper: UserDataMapper
) : ViewModel() {

    suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        userRepository.getCurrentUser()
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading()
            )

    suspend fun getChats(page: Int): StateFlow<Resource<List<ChatsUI>>> =
        chatsRepository.getChats(page)
            .flatMapLatest { flowOf(it.map(chatsMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading()
            )
}


