package st.slex.messenger.ui.single_chat.adapter

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.data.model.ChatListModel

class ChatsDiffUtilCallback(
    private val oldList: List<ChatListModel>,
    private val newList: List<ChatListModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]

}