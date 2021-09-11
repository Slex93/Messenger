package st.slex.messenger.utilites.base

import android.view.View

class CardClickListener(
    val clickListener: (View, String) -> Unit
) {
    fun onClick(p0: View, url: String) = clickListener(p0, url)
}