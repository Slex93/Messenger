package st.slex.messenger.single_chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.activity.activity_model.ActivityConst.CURRENT_UID
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.common.messenger.single_chat.model.Message

class ChatAdapter: RecyclerView.Adapter<ChatViewHolder>() {

    private var listMessages = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerSingleChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder((binding))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = listMessages[position]
        if (message.from == CURRENT_UID){
            holder.bindUser(message)
        } else {
            holder.bindReceiver(message)
        }

    }

    override fun getItemCount(): Int = listMessages.size

    fun addItemToBottom(item: Message, onSuccess: () -> Unit) {
        if (!listMessages.contains(item)) {
            listMessages.add(item)
            notifyItemInserted(listMessages.size)
        }
        onSuccess()
    }

    fun addItemToTop(item: Message, onSuccess: () -> Unit) {
        if (!listMessages.contains(item)) {
            listMessages.add(item)
            listMessages.sortBy { it.timestamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }

}