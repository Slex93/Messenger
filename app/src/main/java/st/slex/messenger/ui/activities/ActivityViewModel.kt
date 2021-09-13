package st.slex.messenger.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import javax.inject.Inject

class ActivityViewModel @Inject constructor(
    private val repository: ActivityRepository
) : ViewModel() {

    fun updateContacts(listContacts: List<ContactModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateContacts(listContacts)
    }

    fun changeState(state: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.changeState(state)
    }

}