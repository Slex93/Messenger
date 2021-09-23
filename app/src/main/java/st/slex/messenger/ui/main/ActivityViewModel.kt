package st.slex.messenger.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import st.slex.messenger.data.contacts.ContactModel
import st.slex.messenger.data.main_activity.ActivityRepository
import st.slex.messenger.ui.core.VoidUIResponse
import st.slex.messenger.ui.core.VoidUIResult
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository,
    private val response: VoidUIResponse
) : ViewModel() {

    suspend fun updateContacts(list: List<ContactModel>): StateFlow<VoidUIResult> =
        response.create(repository.updateContacts(list)).stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidUIResult.Loading
        )

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}