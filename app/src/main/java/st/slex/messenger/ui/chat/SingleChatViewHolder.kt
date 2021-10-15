package st.slex.messenger.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.messenger.data.chat.MessageModel
import st.slex.messenger.utilites.funs.convertToTime

class SingleChatViewHolder(
    private val binding: ItemRecyclerSingleChatBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindUser(message: MessageModel) {
        binding.chatBlockUserMessage.visibility = View.VISIBLE
        binding.chatBlockReceiverMessage.visibility = View.GONE
        binding.chatUserMessage.text = message.text
        binding.chatUserMessageTime.text = message.timestamp.toString().convertToTime()
    }

    fun bindReceiver(message: MessageModel) {
        binding.chatBlockReceiverMessage.visibility = View.VISIBLE
        binding.chatBlockUserMessage.visibility = View.GONE
        binding.chatReceiverMessage.text = message.text
        binding.chatReceiverMessageTime.text = message.timestamp.toString().convertToTime()
    }
}