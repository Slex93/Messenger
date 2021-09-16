package st.slex.messenger.ui.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.ui.contacts.ContactsUI
import st.slex.messenger.ui.core.ClickListener

class ContactAdapter(
    private val clickListener: ClickListener<ContactsUI>,
) : RecyclerView.Adapter<ContactViewHolder>() {

    private var contacts = mutableListOf<ContactsUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    fun addItems(data: List<ContactsUI>) {
        val result = DiffUtil.calculateDiff(ContactsDiffUtilCallback(contacts, data))
        contacts.clear()
        contacts.addAll(data)
        result.dispatchUpdatesTo(this)
    }

}