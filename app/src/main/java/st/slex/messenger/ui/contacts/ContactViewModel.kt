package st.slex.messenger.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.ContactsRepository
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

class ContactViewModel @Inject constructor(private val repository: ContactsRepository) :
    ViewModel() {


    suspend fun getContact(): StateFlow<Response<UserModel>> = repository.getContacts().stateIn(
        viewModelScope
    )

    fun initContact(): LiveData<Response<UserModel>> = liveData(Dispatchers.IO) {
        emit(Response.Loading)
        try {
            repository.getContacts().collect {
                emit(it)
            }
        } catch (exception: Exception) {
            emit(Response.Failure(exception = exception))
        }
    }
}