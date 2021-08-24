package st.slex.messenger.contacts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.contacts.ContactClickListener
import st.slex.common.messenger.contacts.ContactFragment
import st.slex.common.messenger.contacts.model.Contact
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding

class ContactAdapter(private val clickListener: ContactClickListener, private val fragment: ContactFragment) :
    RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList = mutableListOf<Contact>()
    private var contactListNew = mutableListOf<Contact>()

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

    fun addItems(contact: Contact) {
        if (!contactListNew.contains(contact)){
            contactListNew.add(contact)
            contactListNew.sortBy { it.fullname }
            Log.i("Transit::AdapterAdd", contact.toString())
        }
        contactList = contactListNew
        notifyDataSetChanged()

    }

}