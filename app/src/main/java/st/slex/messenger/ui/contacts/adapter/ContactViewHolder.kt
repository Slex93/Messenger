package st.slex.messenger.ui.contacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.Contact
import st.slex.messenger.ui.contacts.ContactClickListener
import st.slex.messenger.utilites.downloadAndSet

class ContactViewHolder(private val binding: ItemRecyclerContactBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var contact: Contact
    private lateinit var clickListener: ContactClickListener
    private lateinit var key: String
    private val card = binding.itemContactCard

    fun bind(contact: Contact, position: Int) {
        this.contact = contact
        binding.recyclerContactUsername.text = contact.fullname
        binding.recyclerContactPhone.text = contact.phone
        binding.recyclerContactImage.downloadAndSet(contact.url)
        key = "card$position"
        binding.itemContactCard.transitionName = key
    }

    fun clickListener(clickListener: ContactClickListener) {
        this.clickListener = clickListener
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener.onClick(binding.itemContactCard, contact, key)
    }

}