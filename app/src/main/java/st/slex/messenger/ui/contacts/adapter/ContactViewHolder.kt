package st.slex.messenger.ui.contacts.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.funs.downloadAndSet

class ContactViewHolder(private val binding: ItemRecyclerContactBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var clickListener: CardClickListener

    fun bind(contact: ContactModel) {
        binding.itemContactCard.transitionName = contact.id
        binding.recyclerContactUsername.text = contact.full_name
        binding.recyclerContactPhone.text = contact.phone
        binding.recyclerContactImage.downloadAndSet(contact.url)
    }

    fun clickListener(clickListener: CardClickListener) {
        this.clickListener = clickListener
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        clickListener.onClick(binding.itemContactCard)
    }

}