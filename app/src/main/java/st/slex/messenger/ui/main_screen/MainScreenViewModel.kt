package st.slex.messenger.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Response
import st.slex.messenger.core.TestResponse
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.data.repository.interf.UserRepository
import st.slex.messenger.domain.chats.ChatsDomain
import st.slex.messenger.domain.chats.ChatsInteractor
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainScreenViewModel @Inject constructor(
    private val repository: MainRepository,
    private val userRepository: UserRepository,
    private val interactor: ChatsInteractor,
    private val mapper: ChatsDomain.ChatsDomainMapper<ChatsUI>
) : ViewModel() {

    suspend fun getChatList(page: Int): StateFlow<Response<List<ChatListModel>>> =
        repository.getChatList(page).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Response.Loading
        )

    suspend fun currentUser(): StateFlow<Response<UserModel>> =
        userRepository.getCurrentUser().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Response.Loading
        )

    suspend fun getChats(page: Int): StateFlow<TestResponse<List<ChatsUI>>> =
        interactor.getChatsList(page).mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = TestResponse.Loading
        )

    private suspend fun Flow<TestResponse<List<ChatsDomain>>>.mapIt(): Flow<TestResponse<List<ChatsUI>>> =
        callbackFlow {
            try {
                this@mapIt.collect {
                    when (it) {
                        is TestResponse.Success -> {
                            val result = it.value.map { data ->
                                data.map(mapper)
                            }
                            trySendBlocking(TestResponse.Success(result))
                        }
                        is TestResponse.Failure -> {
                            trySendBlocking(TestResponse.Failure(it.exception))
                        }
                    }
                }
            } catch (exception: Exception) {
                trySendBlocking(TestResponse.Failure(exception))
            }
            awaitClose { }
        }

}


