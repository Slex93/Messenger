package st.slex.messenger.main.ui.chats

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.core.Resource

class ChatsDiffUtilCallback(
    private val oldList: List<Resource<ChatsUI>>,
    private val newList: List<Resource<ChatsUI>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is Resource.Success && newItem is Resource.Success) {
            oldItem.data.gettingId() == newItem.data.gettingId()
        } else false
    }


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is Resource.Success && newItem is Resource.Success) {
            oldItem.data.same(newItem.data)
        } else true
    }
}