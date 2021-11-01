package st.slex.messenger.main.ui.contacts

import androidx.recyclerview.widget.DiffUtil

class ContactsDiffUtilCallback(
    private val oldList: List<ContactUI>,
    private val newList: List<ContactUI>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].getId == newList[newItemPosition].getId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].getPhone == newList[newItemPosition].getPhone
}