package st.slex.messenger.ui.main_screen.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.ui.main_screen.ChatsUI

abstract class MainViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(item: ChatsUI) {
    }

    class Base(
        private val binding: ItemRecyclerMainBinding,
        private val clickListener: ClickListener<ChatsUI>
    ) : MainViewHolder(binding.root) {
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