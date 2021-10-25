package st.slex.messenger.ui.user_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.auth.core.Resource
import st.slex.messenger.data.user.UserDataMapper
import st.slex.messenger.data.user.UserRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserViewModel
@Inject constructor(
    private val repository: UserRepository,
    private val mapper: UserDataMapper,
) : ViewModel() {

    suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        repository.getCurrentUser()
            .flatMapLatest { flowOf(it.map(mapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.Loading
            )

    suspend fun saveUsername(username: String): StateFlow<Resource<Nothing?>> =
        repository.saveUsername(username).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading
        )

    suspend fun saveImage(uri: Uri): StateFlow<Resource<Nothing?>> =
        repository.saveImage(uri).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading
        )
}