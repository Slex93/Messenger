package st.slex.messenger.auth.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st.slex.messenger.auth.model.AuthRepository
import st.slex.messenger.auth.model.base.AuthUser
import st.slex.messenger.data.repository.AuthRepositoryImpl
import st.slex.messenger.utilites.result.AuthResult

class AuthViewModel(private val repository: AuthRepository, private val repo: AuthRepositoryImpl) :
    ViewModel() {
    private var _authResult = MutableLiveData<AuthResult<AuthUser>>()
    val authResult: LiveData<AuthResult<AuthUser>> get() = repo.user

    fun authPhone(activity: Activity, phone: String) = viewModelScope.launch {
        repo.signInWithPhone(phone, activity)
    }

    val callbackReturnStatus = repository.callbackReturnStatus

    fun initPhoneNumber(phone: String, activity: Activity) {
        repository.initPhoneNumber(phone, activity)
    }


    fun postCode(code: String) {
        repository.postCode(code)
    }

}