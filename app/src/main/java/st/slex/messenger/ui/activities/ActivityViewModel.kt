package st.slex.messenger.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import st.slex.messenger.data.contacts.ContactModel
import st.slex.messenger.data.main_activity.ActivityRepository
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    suspend fun updateContacts(list: List<ContactModel>): StateFlow<VoidResponse> =
        repository.updateContacts(list).stateIn(
            viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidResponse.Loading
        )

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}