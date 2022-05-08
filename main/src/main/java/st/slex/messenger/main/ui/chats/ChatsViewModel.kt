package st.slex.messenger.main.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.core.Resource
import st.slex.messenger.main.data.user.UserDataMapper
import st.slex.messenger.main.data.user.UserRepository
import st.slex.messenger.main.domain.ChatsInteractor
import st.slex.messenger.main.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserDataMapper,
    private val chatsInteractor: ChatsInteractor
) : ViewModel() {

    suspend fun getChats(pageNumber: Int): StateFlow<Resource<List<Resource<ChatsUI>>>> =
        chatsInteractor.getAllChats(pageNumber)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Resource.Loading
            )

    suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        userRepository.getUser()
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )
}


