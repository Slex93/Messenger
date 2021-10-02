package st.slex.messenger.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import st.slex.messenger.core.DataResult
import st.slex.messenger.data.chat.MessageModel
import st.slex.messenger.data.chat.SingleChatRepository
import st.slex.messenger.domain.user.UserDomainMapper
import st.slex.messenger.domain.user.UserDomainResult
import st.slex.messenger.domain.user.UserInteractor
import st.slex.messenger.ui.user_profile.UserUI
import st.slex.messenger.ui.user_profile.UserUiResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SingleChatViewModel @Inject constructor(
    private val repository: SingleChatRepository,
    private val interactor: UserInteractor,
    private val mapper: UserDomainMapper<UserUiResult>
) : ViewModel() {

    suspend fun getUser(uid: String): StateFlow<UserUiResult> =
        interactor.getUser(uid).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserUiResult.Loading
        )

    private suspend fun currentUser(): StateFlow<UserUiResult> =
        interactor.getCurrentUser().mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UserUiResult.Loading
        )

    fun getMessages(uid: String, limitToLast: Int): LiveData<DataResult<MessageModel>> =
        liveData(Dispatchers.IO) {
            try {
                repository.getMessages(uid = uid, limitToLast = limitToLast).collect {
                    emit(it)
                }
            } catch (exception: Exception) {
                emit(DataResult.Failure(exception = exception))
            }
        }

    fun sendMessage(message: String, user: UserUI) = viewModelScope.launch {
        currentUser().collect {
            when (it) {
                is UserUiResult.Success -> {
                    repository.sendMessage(message = message, user = user, it.data)
                }
                is UserUiResult.Failure -> {

                }
                is UserUiResult.Loading -> {

                }
            }
        }
    }

    private suspend fun Flow<UserDomainResult>.mapIt(): Flow<UserUiResult> = callbackFlow {
        this@mapIt.collect {
            trySendBlocking(it.map(mapper))
        }
        awaitClose { }
    }

}