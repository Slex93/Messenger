package com.st.slex.common.messenger.auth.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.st.slex.common.messenger.auth.model.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    val callbackReturnStatus = repository.callbackReturnStatus

    fun initPhoneNumber(phone: String, activity: Activity) {
        repository.initPhoneNumber(phone, activity)
    }

    fun postCode(code: String) {
        repository.postCode(code)
    }

}