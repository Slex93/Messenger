package st.slex.messenger.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.settings.SettingsRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    suspend fun signOut(state: String): StateFlow<VoidResponse> =
        repository.signOut(state).stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidResponse.Loading
        )
}