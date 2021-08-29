package st.slex.messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.data.repository.interf.ActivityRepository
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val repository: ActivityRepository) :
    ViewModel() {

    fun isAuthorise(): LiveData<Resource<FirebaseAuth>> = liveData {
        Resource.Loading
        try {
            repository.isAuthorise().collect {
                when (it) {
                    is Resource.Success -> {
                        emit(Resource.Success(it.data))
                    }
                    is Resource.Failure -> {
                        emit(Resource.Failure(it.exception))
                    }
                    else -> {

                    }
                }
            }
        } catch (exception: Exception) {
            emit(Resource.Failure(exception))
        }
    }

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