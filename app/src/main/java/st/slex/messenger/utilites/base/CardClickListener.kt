package st.slex.messenger.utilites.base

import android.view.View

class CardClickListener(
    val clickListener: (View) -> Unit
) : View.OnClickListener {
    override fun onClick(p0: View) = clickListener(p0)
}