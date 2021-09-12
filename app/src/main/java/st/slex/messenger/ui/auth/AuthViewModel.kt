package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.domain.interactor.interf.AuthInteractor
import st.slex.messenger.utilites.result.AuthResponse
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val interactor: AuthInteractor
) : ViewModel() {

    suspend fun login(phone: String, activity: Activity): StateFlow<AuthResponse> =
        interactor.login(phone, activity).stateIn(viewModelScope)

    suspend fun sendCode(id: String, code: String): StateFlow<AuthResponse> =
        interactor.sendCode(id, code).stateIn(viewModelScope)

}