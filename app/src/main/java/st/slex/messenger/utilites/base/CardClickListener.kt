package st.slex.messenger.utilites.base

import com.google.android.material.card.MaterialCardView

class CardClickListener(
    val clickListener: (MaterialCardView) -> Unit
) {
    fun onClick(
        cardView: MaterialCardView,
    ) = clickListener(cardView)
}