package st.slex.messenger.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.data.chat.SingleChatRepository
import st.slex.messenger.data.contacts.ContactsRepository
import st.slex.messenger.data.profile.UserRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleChatViewModel @Inject constructor(
    private val repository: SingleChatRepository,
    private val userRepository: UserRepository,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    suspend fun getChatUIHead(uid: String): StateFlow<Resource<UserSingleChatUI>> =
        userRepository.getUserState(uid)
            .combine(contactsRepository.getContactFullName(uid)) { userResult, contactResult ->
                when (userResult) {
                    is Resource.Success ->
                        when (contactResult) {
                            is Resource.Success -> Resource.Success(
                                UserSingleChatUI.Base(
                                    state = userResult.data,
                                    fullName = contactResult.data
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

    suspend fun sendMessage(receiverId: String, message: String): StateFlow<Resource<Nothing?>> =
        flowOf(repository.sendMessage(receiverId = receiverId, message = message)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading
        )
}