package st.slex.messenger.ui.single_chat.adapter

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.ui.main_screen.ChatsUI

class ChatsDiffUtilCallback(
    private val oldList: List<ChatsUI>,
    private val newList: List<ChatsUI>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[newItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]

}