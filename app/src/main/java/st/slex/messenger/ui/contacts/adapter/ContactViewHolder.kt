package st.slex.messenger.ui.contacts.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.base.SetImageWithGlide

class ContactViewHolder(private val binding: ItemRecyclerContactBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var _url: String? = null
    private val url get() = _url!!

    fun bind(contact: ContactModel, glide: SetImageWithGlide) {
        _url = contact.url
        binding.itemContactCard.transitionName = contact.id
        binding.recyclerContactUsername.text = contact.full_name
        binding.recyclerContactPhone.text = contact.phone
        glide.setImage(
            binding.recyclerContactImage,
            url,
            needCrop = true,
            needCircleCrop = true
        )
    }

    fun clickListener(clickListener: CardClickListener) {
        binding.itemContactCard.setOnClickListener {
            clickListener.onClick(it, url)
        }
    }

}