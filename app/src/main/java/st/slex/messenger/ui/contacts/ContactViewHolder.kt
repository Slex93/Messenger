package st.slex.messenger.ui.contacts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.ui.core.ClickListener

abstract class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: ContactUI)

    class Base(
        private val binding: ItemRecyclerContactBinding,
        private val clickListener: ClickListener<ContactUI>
    ) : ContactViewHolder(binding.root) {

        override fun bind(item: ContactUI) {
            with(binding) {
                item.bind(
                    phoneTextView = phoneTextView,
                    nameTextView = usernameTextView,
                    imageView = avatarImageView,
                    card = itemCardView
                )
                itemCardView.setOnClickListener(item.onClickListener)
            }
        }

        private val ContactUI.onClickListener: View.OnClickListener
            get() = View.OnClickListener {
                clickListener.click(this)
            }
    }
}