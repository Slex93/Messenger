package st.slex.messenger.ui.chat

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.data.chat.MessageModel

class MessagesDiffItemCallback(
    private val oldItem: MessageModel,
    private val newItem: MessageModel
) : DiffUtil.ItemCallback<MessageModel>() {

    override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
        oldItem.timestamp == newItem.timestamp

    override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
        oldItem.text == newItem.text

}