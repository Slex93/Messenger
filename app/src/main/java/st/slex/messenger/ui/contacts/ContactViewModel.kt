package st.slex.messenger.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val repository: ContactsRepository) :
    ViewModel() {

    fun initContact(): LiveData<Resource<ContactModel>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            repository.getContacts().collect {
                emit(it)
            }
        } catch (exception: Exception) {
            emit(Resource.Failure(exception = exception))
        }
    }
}