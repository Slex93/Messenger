package st.slex.messenger.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.auth.core.Resource
import st.slex.messenger.data.settings.SettingsRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    suspend fun signOut(state: String): StateFlow<Resource<Nothing?>> =
        flowOf(repository.signOut(state)).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading
        )
}