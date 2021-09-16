package st.slex.messenger.ui.chats.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.core.ClickListener

abstract class ChatsViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(item: ChatsUI) = Unit

    class Base(
        private val binding: ItemRecyclerMainBinding,
        private val clickListener: ClickListener<ChatsUI>
    ) : ChatsViewHolder(binding.root) {
        override fun bind(item: ChatsUI) = with(binding) {
            item.map(
                this.usernameTextView,
                this.messageTextView,
                this.photoImageView,
                this.timestampTextView,
                this.itemCardView
            )
            itemCardView.setOnClickListener {
                clickListener.click(item)
            }
        }
    }
}