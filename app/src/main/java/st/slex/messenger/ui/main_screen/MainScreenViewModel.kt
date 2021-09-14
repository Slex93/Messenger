package st.slex.messenger.ui.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.data.repository.interf.UserRepository
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainScreenViewModel @Inject constructor(
    private val repository: MainRepository,
    private val userRepository: UserRepository
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

}