package com.st.slex.common.messenger.activity.activity_view_model

import androidx.lifecycle.ViewModel
import com.st.slex.common.messenger.activity.activity_model.ActivityRepository
import com.st.slex.common.messenger.activity.activity_model.Contact
import com.st.slex.common.messenger.activity.activity_model.User

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {

    val getUserForHeader = repository.getUserForHeader

    fun initFirebase() {
        repository.initFirebase()
    }

    fun signOut() {
        repository.signOut()
    }

    fun initUser() {
        repository.initUser()
    }

    fun updatePhoneToDatabase(listContacts: List<Contact>) {
        repository.updatePhonesToDatabase(listContacts)
    }

    fun statusOnline(){
        repository.statusOnline()
    }

    fun statusOffline(){
        repository.statusOffline()
    }

}