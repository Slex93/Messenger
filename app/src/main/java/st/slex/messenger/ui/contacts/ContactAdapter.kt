package st.slex.messenger.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.ui.core.ClickListener

class ContactAdapter(
    options: DatabasePagingOptions<ContactUIModel>,
    private val clickListener: ClickListener<ContactUIModel>
) : FirebaseRecyclerPagingAdapter<ContactUIModel, ContactViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
        model: ContactUIModel
    ) {
        holder.bind(model)
    }
}