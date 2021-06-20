package com.st.slex.common.messenger.contacts.viewmodel

import androidx.lifecycle.ViewModel
import com.st.slex.common.messenger.contacts.model.ContactRepository

class ContactViewModel(private val repository: ContactRepository):ViewModel() {

    val contact = repository.contact
    val flag = repository.flag
    fun initContact(){
        repository.getContacts()
    }
}