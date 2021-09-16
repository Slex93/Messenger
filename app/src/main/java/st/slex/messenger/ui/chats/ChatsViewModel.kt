package st.slex.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Response
import st.slex.messenger.core.TestResponse
import st.slex.messenger.data.profile.UserModel
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.domain.chats.ChatsDomainMapper
import st.slex.messenger.domain.chats.ChatsDomainResult
import st.slex.messenger.domain.chats.ChatsInteractor
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val interactor: ChatsInteractor,
    private val mapper: ChatsDomainMapper<ChatsUIResult>
) : ViewModel() {

    suspend fun currentUser(): StateFlow<Response<UserModel>> =
        userRepository.getCurrentUser().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Response.Loading
        )

    suspend fun getChats(page: Int): StateFlow<ChatsUIResult> =
        interactor.getChatsList(page).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatsUIResult.Loading
        )

    private suspend fun Flow<ChatsDomainResult>.mapIt(): Flow<ChatsUIResult> =
        callbackFlow {
            try {
                this@mapIt.collect {
                    trySendBlocking(it.map(mapper))
                }
            } catch (exception: Exception) {
                trySendBlocking(TestResponse.Failure(exception))
            }
            awaitClose { }
        }

}


