package st.slex.messenger.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import st.slex.messenger.data.repository.interf.AuthRepository
import st.slex.messenger.utilites.result.AuthResponse
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    suspend fun authPhone(activity: Activity, phone: String): StateFlow<AuthResponse> =
        repository.signInWithPhone(activity, phone)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = AuthResponse.Loading
            )

    suspend fun sendCode(id: String, code: String): StateFlow<AuthResponse> =
        repository.sendCode(id, code)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = AuthResponse.Loading
            )

    suspend fun authUser(): StateFlow<VoidResponse> = repository.authUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = VoidResponse.Loading
        )

}