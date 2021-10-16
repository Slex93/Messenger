package st.slex.messenger.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding

abstract class SingleChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bindUser(message: MessageUI)
    abstract fun bindReceiver(message: MessageUI)

    class Base(
        private val binding: ItemRecyclerSingleChatBinding
    ) : SingleChatViewHolder(binding.root) {

        override fun bindUser(message: MessageUI) = with(binding) {
            message.bindMessage(
                hideLayout = chatBlockReceiverMessage,
                showLayout = chatBlockUserMessage,
                messageTextView = chatUserMessage,
                timeStampTextView = chatUserMessageTime
            )
        }

        override fun bindReceiver(message: MessageUI) = with(binding) {
            message.bindMessage(
                hideLayout = chatBlockUserMessage,
                showLayout = chatBlockReceiverMessage,
                messageTextView = chatReceiverMessage,
                timeStampTextView = chatReceiverMessageTime
            )
        }
    }
}