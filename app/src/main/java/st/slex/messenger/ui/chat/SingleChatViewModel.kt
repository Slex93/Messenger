package st.slex.messenger.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import st.slex.messenger.core.Abstract
import st.slex.messenger.data.chat.SingleChatRepository
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleChatViewModel @Inject constructor(
    private val repository: SingleChatRepository,
    private val userRepository: UserRepository,
    private val mapper: Abstract.Mapper.DataToUi<UserData, UIResult<UserUI>>
) : ViewModel() {

    suspend fun getUser(uid: String): StateFlow<UIResult<UserUI>> =
        userRepository.getUser(uid).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    private suspend fun currentUser(): StateFlow<UIResult<UserUI>> =
        userRepository.getCurrentUser().mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    fun sendMessage(message: String, user: UserUI) = viewModelScope.launch {
        Log.i("MessagesTest:User:", user.toString())
        currentUser().collect {
            when (it) {
                is UIResult.Success -> {
                    Log.i("MessagesTest:CurrentUser:", it.data.toString())
                    repository.sendMessage(message = message, user = user, it.data).collect()
                }
                is UIResult.Failure -> {

                }
                is UIResult.Loading -> {

                }
            }
        }
    }

    private suspend fun Flow<DataResult<UserData>>.mapIt(): Flow<UIResult<UserUI>> = callbackFlow {
        this@mapIt.collect {
            trySendBlocking(it.map(mapper))
        }
        awaitClose { }
    }

}