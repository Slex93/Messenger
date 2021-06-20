package com.st.slex.common.messenger.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.st.slex.common.messenger.contacts.ContactClickListener
import com.st.slex.common.messenger.contacts.model.Contact
import com.st.slex.common.messenger.databinding.ItemRecyclerContactBinding

class ContactAdapter(private val clickListener: ContactClickListener) :
    RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList = mutableListOf<Contact>()

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

    fun addItems(contact: Contact) {
        contactList.add(contact)
        notifyDataSetChanged()
    }
}