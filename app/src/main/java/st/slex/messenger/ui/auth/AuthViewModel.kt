package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.data.repository.impl.AuthRepositoryImpl
import st.slex.messenger.utilites.result.AuthResult

class AuthViewModel(private val repository: AuthRepositoryImpl) : ViewModel() {

    val authResultModel: LiveData<AuthResult<AuthUserModel>> get() = repository.userModel

    fun authPhone(activity: Activity, phone: String) = viewModelScope.launch {
        repository.signInWithPhone(phone, activity)
    }

    fun sendCode(id: String, code: String) = viewModelScope.launch {
        repository.sendCode(id = id, code = code)
    }

    fun authUser(authUserModel: AuthUserModel) = viewModelScope.launch {
        repository.authUser(authUserModel = authUserModel)
    }

}