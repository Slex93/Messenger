package st.slex.messenger.ui.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.core.Response
import st.slex.messenger.data.profile.UserModel
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    suspend fun currentUser(): StateFlow<Response<UserModel>> =
        repository.getCurrentUser().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Response.Loading
        )

    suspend fun saveUsername(username: String): StateFlow<VoidResponse> =
        repository.saveUsername(username).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidResponse.Loading
        )
}