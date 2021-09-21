package st.slex.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.domain.chats.ChatsDomainMapper
import st.slex.messenger.domain.chats.ChatsDomainResult
import st.slex.messenger.domain.chats.ChatsInteractor
import st.slex.messenger.domain.user.UserDomainMapper
import st.slex.messenger.domain.user.UserDomainResult
import st.slex.messenger.domain.user.UserInteractor
import st.slex.messenger.ui.user_profile.UserUiResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val interactor: ChatsInteractor,
    private val mapper: ChatsDomainMapper<ChatsUIResult>,
    private val userMapper: UserDomainMapper<UserUiResult>
) : ViewModel() {

    suspend fun currentUser(): StateFlow<UserUiResult> =
        userInteractor.getCurrentUser().mapUser().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserUiResult.Loading
        )

    suspend fun getChats(page: Int): StateFlow<ChatsUIResult> =
        interactor.getChatsList(page).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatsUIResult.Loading
        )

    private suspend fun Flow<ChatsDomainResult>.mapIt(): Flow<ChatsUIResult> =
        callbackFlow {
            this@mapIt.response { trySendBlocking(it) }
            awaitClose { }
        }

    private suspend inline fun Flow<ChatsDomainResult>.response(crossinline function: (ChatsUIResult) -> Unit) =
        try {
            this.collect {
                function(it.map(mapper))
            }
        } catch (exception: Exception) {
            function(ChatsUIResult.Failure(exception))
        }

    private suspend fun Flow<UserDomainResult>.mapUser(): Flow<UserUiResult> = callbackFlow {
        this@mapUser.responseUser { trySendBlocking(it) }
        awaitClose { }
    }

    private suspend inline fun Flow<UserDomainResult>.responseUser(crossinline function: (UserUiResult) -> Unit) =
        try {
            this.collect {
                function(it.map(userMapper))
            }
        } catch (exception: Exception) {
            function(UserUiResult.Failure(exception))
        }

}


