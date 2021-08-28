package st.slex.messenger.ui.contacts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.ui.contacts.ContactClickListener

class ContactAdapter(
    private val clickListener: ContactClickListener
) :
    RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList = mutableListOf<ContactModel>()
    private var contactListNew = mutableListOf<ContactModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position], position)
        holder.clickListener(clickListener)
    }

    override fun getItemCount(): Int = contactList.size

    fun addItems(contact: ContactModel) {
        if (!contactListNew.contains(contact)) {
            contactListNew.add(contact)
            contactListNew.sortBy { it.fullname }
            Log.i("Transit::AdapterAdd", contact.toString())
        }
        contactList = contactListNew
        notifyDataSetChanged()
    }

}