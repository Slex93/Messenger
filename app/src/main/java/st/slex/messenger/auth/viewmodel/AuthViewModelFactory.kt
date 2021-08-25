package st.slex.messenger.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import st.slex.messenger.auth.model.AuthRepository
import st.slex.messenger.data.repository.AuthRepositoryImpl

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val repo: AuthRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}