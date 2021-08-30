package st.slex.messenger.ui.contacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.ui.contacts.ContactClickListener
import st.slex.messenger.utilites.funs.downloadAndSet

class ContactViewHolder(private val binding: ItemRecyclerContactBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var clickListener: ContactClickListener

    fun bind(contact: UserModel) {
        binding.itemContactCard.transitionName = contact.id
        binding.recyclerContactUsername.text = contact.full_name
        binding.recyclerContactPhone.text = contact.phone
        binding.recyclerContactImage.downloadAndSet(contact.url)
    }

    fun clickListener(clickListener: ContactClickListener) {
        this.clickListener = clickListener
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener.onClick(binding.itemContactCard)
    }

}