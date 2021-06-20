package com.st.slex.common.messenger.contacts.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.st.slex.common.messenger.contacts.ContactClickListener
import com.st.slex.common.messenger.contacts.model.Contact
import com.st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import com.st.slex.common.messenger.utilites.downloadAndSet

class ContactViewHolder(private val binding: ItemRecyclerContactBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var contact: Contact
    private lateinit var clickListener: ContactClickListener
    private lateinit var key: String
    val card = binding.itemContactCard

    fun bind(contact: Contact, position: Int) {
        this.contact = contact
        binding.recyclerContactUsername.text = contact.fullname
        binding.recyclerContactPhone.text = contact.phone
        binding.recyclerContactImage.downloadAndSet(contact.url)
        key = "card$position"
        /*binding.itemContactCard.transitionName = key*/
        Log.i("Transit::Bind", key)
    }

    fun clickListener(clickListener: ContactClickListener) {
        this.clickListener = clickListener
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener.onClick(binding.itemContactCard, contact, key)
    }

}