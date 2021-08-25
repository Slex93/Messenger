package st.slex.messenger.ui.contacts.viewmodel

import androidx.lifecycle.ViewModel
import st.slex.messenger.ui.contacts.model.ContactRepository

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    val contact = repository.contact
    val flag = repository.flag
    fun initContact() {
        repository.getContacts()
    }
}