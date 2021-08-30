package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.data.repository.interf.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    fun authPhone(activity: Activity, phone: String) = liveData(Dispatchers.IO) {
        repository.signInWithPhone(phone, activity).collect {
            emit(it)
        }
    }

    fun sendCode(id: String, code: String) = liveData(Dispatchers.IO) {
        repository.sendCode(id, code).collect {
            emit(it)
        }
    }

    fun authUser() = viewModelScope.launch {
        repository.authUser()
    }

}