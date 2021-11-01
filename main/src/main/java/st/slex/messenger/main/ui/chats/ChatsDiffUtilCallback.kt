package st.slex.messenger.main.ui.chats

import androidx.recyclerview.widget.DiffUtil

class ChatsDiffUtilCallback(
    private val oldList: List<ChatsUI>,
    private val newList: List<ChatsUI>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].gettingId() == newList[newItemPosition].gettingId()


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].same(newList[newItemPosition])
}