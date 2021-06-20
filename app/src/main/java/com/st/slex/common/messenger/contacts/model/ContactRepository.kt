package com.st.slex.common.messenger.contacts.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.st.slex.common.messenger.activity.activity_model.ActivityConst
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_PHONE_CONTACT
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_USER
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.REF_DATABASE_ROOT
import com.st.slex.common.messenger.utilites.AppValueEventListener

class ContactRepository {

    val contactList = MutableLiveData<Contact>()

    fun initContact() {
        REF_DATABASE_ROOT
            .child(NODE_PHONE_CONTACT)
            .child(ActivityConst.CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val listOfPrimaryContacts = it.children.map { snapshot ->
                    snapshot.getValue(Contact::class.java) ?: Contact()
                }
                Log.i("Transit::lisOfPContacts", listOfPrimaryContacts.toString())
                listOfPrimaryContacts.forEach { itemContact->
                    REF_DATABASE_ROOT
                        .child(NODE_USER)
                        .child(itemContact.id)
                        .addValueEventListener(AppValueEventListener{
                            val contactPrimary = it.getValue(Contact::class.java)?:Contact()
                            val contact = contactPrimary.copy(fullname = itemContact.fullname)
                            contactList.value = contact
                        })
                }
            })
    }
}

