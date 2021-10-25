package st.slex.messenger.main.ui.chats

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import st.slex.messenger.main.databinding.ItemRecyclerMainBinding
import st.slex.messenger.main.ui.core.ClickListener

abstract class ChatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: ChatsUI)
    abstract fun bindLoading()
    abstract fun bindError(message: String)

    class Base(
        private val binding: ItemRecyclerMainBinding,
        private val clickListener: ClickListener<ChatsUI>
    ) : ChatsViewHolder(binding.root) {

        override fun bind(item: ChatsUI) = with(binding) {
            SHOWPROGRESS.visibility = View.GONE
            item.map(
                userName = usernameTextView,
                userMessage = messageTextView,
                userAvatar = photoImageView,
                userTimestamp = timestampTextView,
                userCardView = itemCardView
            )
            itemCardView.setOnClickListener(item.onClickListener)
        }

        override fun bindLoading() {
            binding.SHOWPROGRESS.visibility = View.VISIBLE
        }

        override fun bindError(message: String) {
            binding.SHOWPROGRESS.visibility = View.GONE
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }

        private val ChatsUI.onClickListener: View.OnClickListener
            get() = View.OnClickListener {
                clickListener.click(this)
            }
    }
}