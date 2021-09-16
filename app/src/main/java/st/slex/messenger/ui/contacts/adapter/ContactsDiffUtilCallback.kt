package st.slex.messenger.ui.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.ui.contacts.ContactsUI

class ContactsDiffUtilCallback(
    private val oldList: List<ContactsUI>,
    private val newList: List<ContactsUI>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}