package com.st.slex.common.messenger.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.st.slex.common.messenger.auth.model.AuthRepository

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModelFactory(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}