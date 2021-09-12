package st.slex.messenger.ui.auth

import androidx.lifecycle.ViewModel
import st.slex.messenger.domain.interactor.interf.LoginInteractor
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    interactor: LoginInteractor
) : ViewModel() {

    suspend fun login(phone: String) {

    }

    suspend fun sendCode(id: String, code: String) {

    }

    suspend fun initUser() {

    }
}