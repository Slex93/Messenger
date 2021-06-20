package com.st.slex.common.messenger.contacts

import com.google.android.material.card.MaterialCardView
import com.st.slex.common.messenger.contacts.model.Contact

class ContactClickListener(
    val clickListener: (MaterialCardView, Contact, String) -> Unit
) {
    fun onClick(
        cardView: MaterialCardView,
        contact: Contact,
        key: String
    ) = clickListener(cardView, contact, key)
}