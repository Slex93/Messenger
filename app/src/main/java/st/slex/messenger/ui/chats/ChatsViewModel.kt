package st.slex.messenger.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Abstract
import st.slex.messenger.data.chats.ChatsData
import st.slex.messenger.data.chats.ChatsRepository
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatsRepository: ChatsRepository,
    private val mapper: Abstract.Mapper.DataToUi<List<ChatsData>, UIResult<List<ChatsUI>>>,
    private val userMapper: Abstract.Mapper.DataToUi<UserData, UIResult<UserUI>>
) : ViewModel() {

    suspend fun currentUser(): StateFlow<UIResult<UserUI>> =
        userRepository.getCurrentUser().mapUser().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    suspend fun getChats(page: Int): StateFlow<UIResult<List<ChatsUI>>> =
        chatsRepository.getChats(page).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    private suspend fun Flow<DataResult<List<ChatsData>>>.mapIt(): Flow<UIResult<List<ChatsUI>>> =
        callbackFlow {
            this@mapIt.response { trySendBlocking(it) }
            awaitClose { }
        }

    private suspend inline fun Flow<DataResult<List<ChatsData>>>.response(
        crossinline function: (UIResult<List<ChatsUI>>) -> Unit
    ) = try {
        this.collect {
            function(it.map(mapper))
        }
    } catch (exception: Exception) {
        function(UIResult.Failure(exception))
    }

    private suspend fun Flow<DataResult<UserData>>.mapUser(): Flow<UIResult<UserUI>> =
        callbackFlow {
            this@mapUser.responseUser { trySendBlocking(it) }
            awaitClose { }
        }

    private suspend inline fun Flow<DataResult<UserData>>.responseUser(
        crossinline function: (UIResult<UserUI>) -> Unit
    ) = try {
        this.collect {
            function(it.map(userMapper))
        }
    } catch (exception: Exception) {
        function(UIResult.Failure(exception))
    }

}


