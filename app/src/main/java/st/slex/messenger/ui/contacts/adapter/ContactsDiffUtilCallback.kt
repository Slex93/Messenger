package st.slex.messenger.ui.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import st.slex.messenger.data.model.ContactModel

class ContactsDiffUtilCallback(
    private val oldList: List<ContactModel>,
    private val newList: List<ContactModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition] == oldList[oldItemPosition]

}