package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.result.VoidResponse
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

    fun authUser(): LiveData<VoidResponse> = liveData(Dispatchers.IO) {
        repository.authUser().collect { emit(it) }
    }

}