package st.slex.messenger.ui.contacts

import com.google.android.material.card.MaterialCardView
import st.slex.messenger.ui.contacts.model.Contact

class ContactClickListener(
    val clickListener: (MaterialCardView, Contact, String) -> Unit
) {
    fun onClick(
        cardView: MaterialCardView,
        contact: Contact,
        key: String
    ) = clickListener(cardView, contact, key)
}