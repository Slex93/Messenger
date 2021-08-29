package st.slex.messenger.ui.single_chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.messenger.data.model.Message
import st.slex.messenger.utilites.funs.asTime

class ChatViewHolder(private val binding: ItemRecyclerSingleChatBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindUser(message: Message) {
        binding.chatBlockUserMessage.visibility = View.VISIBLE
        binding.chatBlockReceiverMessage.visibility = View.GONE
        binding.chatUserMessage.text = message.text
        binding.chatUserMessageTime.text = message.timestamp.toString().asTime()
    }

    fun bindReceiver(message: Message) {
        binding.chatBlockReceiverMessage.visibility = View.VISIBLE
        binding.chatBlockUserMessage.visibility = View.GONE
        binding.chatReceiverMessage.text = message.text
        binding.chatReceiverMessageTime.text = message.timestamp.toString().asTime()
    }

}
