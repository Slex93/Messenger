package st.slex.messenger.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.ui.core.ClickListener

class ContactAdapter(
    options: DatabasePagingOptions<ContactsUI.Base>,
    private val clickListener: ClickListener<ContactsUI>
) : FirebaseRecyclerPagingAdapter<ContactsUI.Base, ContactViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(
        viewHolder: ContactViewHolder,
        position: Int,
        model: ContactsUI.Base
    ) {
        viewHolder.bind(model)
    }
}