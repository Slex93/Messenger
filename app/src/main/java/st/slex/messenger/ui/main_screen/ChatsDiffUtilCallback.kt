package st.slex.messenger.ui.main_screen

import androidx.recyclerview.widget.DiffUtil

class ChatsDiffUtilCallback(
    private val oldList: List<ChatsUI>,
    private val newList: List<ChatsUI>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}