package st.slex.messenger.main.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.settings.SettingsRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    suspend fun signOut(): StateFlow<Resource<Nothing?>> = flow {
        val signOutResult = repository.signOut()
        emit(signOutResult)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = Resource.Loading
    )
}