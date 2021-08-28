package st.slex.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val repository: ActivityRepository) :
    ViewModel() {

    fun initFirebase() = viewModelScope.launch {
        repository.initFirebase()
    }

    fun updatePhoneToDatabase(listContacts: List<ContactModel>) = viewModelScope.launch {
        repository.updatePhonesToDatabase(listContacts)
    }

    fun statusOnline() = viewModelScope.launch {
        repository.statusOnline()
    }

    fun statusOffline() = viewModelScope.launch {
        repository.statusOffline()
    }

}