package st.slex.messenger.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.settings.SettingsRepository
import st.slex.messenger.ui.core.VoidUIResponse
import st.slex.messenger.ui.core.VoidUIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val response: VoidUIResponse
) : ViewModel() {

    suspend fun signOut(state: String): StateFlow<VoidUIResult> =
        response.create(repository.signOut(state)).stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidUIResult.Loading
        )
}