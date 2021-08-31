package st.slex.messenger.ui.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.utilites.base.CardClickListener

class ContactAdapter(
    private val clickListener: CardClickListener
) :
    RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList = mutableListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
        holder.clickListener(clickListener)
    }

    override fun getItemCount(): Int = contactList.size

    fun addItems(contact: UserModel) {
        if (!contactList.contains(contact)) {
            contactList.add(contact)
        }
        notifyDataSetChanged()
    }

}