package com.st.slex.common.messenger.activity.activity_view_model

import androidx.lifecycle.ViewModel
import com.st.slex.common.messenger.activity.activity_model.ActivityRepository

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {

    fun initFirebase() {
        repository.initFirebase()
    }

    fun signOut() {
        repository.signOut()
    }

    fun initUser(function: () -> Unit) {
        repository.initUser(function)
    }


}