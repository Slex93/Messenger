package st.slex.messenger.ui.user_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Abstract
import st.slex.messenger.data.core.DataResult
import st.slex.messenger.data.profile.UserData
import st.slex.messenger.data.profile.UserRepository
import st.slex.messenger.ui.core.UIResult
import st.slex.messenger.ui.core.VoidUIResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserViewModel
@Inject constructor(
    private val repository: UserRepository,
    private val mapper: Abstract.Mapper.DataToUi<UserData, UIResult<UserUI>>,
    private val response: VoidUIResponse
) : ViewModel() {

    suspend fun currentUser(): StateFlow<UIResult<UserUI>> =
        repository.getCurrentUser().mapIt().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    suspend fun saveUsername(username: String): StateFlow<UIResult<*>> =
        response.create(repository.saveUsername(username)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    suspend fun saveImage(uri: Uri): StateFlow<UIResult<*>> =
        response.create(repository.saveImage(uri)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = UIResult.Loading
        )

    private suspend fun Flow<DataResult<UserData>>.mapIt(): Flow<UIResult<UserUI>> = callbackFlow {
        this@mapIt.collect {
            trySendBlocking(it.map(mapper))
        }
        awaitClose { }
    }
}