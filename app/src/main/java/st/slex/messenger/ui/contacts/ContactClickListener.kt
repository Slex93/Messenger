package st.slex.messenger.ui.contacts

import com.google.android.material.card.MaterialCardView
import st.slex.messenger.data.model.ContactModel

class ContactClickListener(
    val clickListener: (MaterialCardView, ContactModel, String) -> Unit
) {
    fun onClick(
        cardView: MaterialCardView,
        contact: ContactModel,
        key: String
    ) = clickListener(cardView, contact, key)
}