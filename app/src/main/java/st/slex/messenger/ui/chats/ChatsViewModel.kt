package st.slex.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.user.UserDataMapper
import st.slex.messenger.data.user.UserRepository
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserDataMapper,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    suspend fun getChatUIHead(chat: ChatsUI): StateFlow<Resource<ChatsUI>> =
        userRepository.getUserUrl(chat.gettingId())
            .combine(contactsRepository.getContactFullName(chat.gettingId())) { userResult, contactResult ->
                when (userResult) {
                    is Resource.Success ->
                        when (contactResult) {
                            is Resource.Success ->
                                Resource.Success(
                                    chat.copy(
                                        full_name = contactResult.data,
                                        url = userResult.data
                                    )
                                )
                            is Resource.Failure -> Resource.Failure(contactResult.exception)
                            is Resource.Loading -> Resource.Loading
                        }
                    is Resource.Failure -> Resource.Failure(userResult.exception)
                    is Resource.Loading -> Resource.Loading
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )

    suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        userRepository.getCurrentUser()
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )
}


