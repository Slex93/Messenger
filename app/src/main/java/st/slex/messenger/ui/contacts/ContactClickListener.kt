package st.slex.messenger.ui.contacts

import com.google.android.material.card.MaterialCardView

class ContactClickListener(
    val clickListener: (MaterialCardView) -> Unit
) {
    fun onClick(
        cardView: MaterialCardView,
    ) = clickListener(cardView)
}