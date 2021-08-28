package st.slex.messenger.ui.contacts.model

import androidx.lifecycle.MutableLiveData
import st.slex.messenger.utilites.Const
import st.slex.messenger.utilites.Const.NODE_PHONE_CONTACT
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT
import st.slex.messenger.utilites.base.AppValueEventListener

class ContactRepository {

    val contact = MutableLiveData<Contact>()
    val flag = MutableLiveData<Boolean>()

    init {
        flag.value = false
    }

    fun getContacts() {
        REF_DATABASE_ROOT
            .child(NODE_PHONE_CONTACT)
            .child(Const.CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val listOfPrimaryContacts = it.children.map { snapshot ->
                    snapshot.getValue(Contact::class.java) ?: Contact()
                }
                val list = mutableListOf<Contact>()
                listOfPrimaryContacts.forEach { itemContact ->
                    REF_DATABASE_ROOT
                        .child(NODE_USER)
                        .child(itemContact.id)
                        .addValueEventListener(AppValueEventListener { upSnapshot ->
                            val contactPrimary =
                                upSnapshot.getValue(Contact::class.java) ?: Contact()
                            val item = contactPrimary.copy(fullname = itemContact.fullname)
                            list.add(item)
                            contact.value = item
                            if (list.size == listOfPrimaryContacts.size) {
                                flag.value = true
                            }

                        })
                }
            })
    }
}

