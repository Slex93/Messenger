package st.slex.messenger.ui.contacts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.ui.core.ClickListener

abstract class ContactViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(item: ContactsUI) = Unit

    class Base(
        private val binding: ItemRecyclerContactBinding,
        private val clickListener: ClickListener<ContactsUI>
    ) : ContactViewHolder(binding.root) {
        override fun bind(item: ContactsUI) = with(binding) {
            item.map(
                userName = usernameTextView,
                userPhone = phoneTextView,
                userAvatar = avatarImageView,
                userCardView = itemCardView
            )
            itemCardView.setOnClickListener {
                clickListener.click(item)
            }
        }
    }
}