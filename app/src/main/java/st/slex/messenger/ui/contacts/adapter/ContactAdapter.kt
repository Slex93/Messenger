package st.slex.messenger.ui.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.data.model.ContactModel
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.base.SetImageWithGlide

class ContactAdapter(
    private val clickListener: CardClickListener,
    private val glide: SetImageWithGlide
) : RecyclerView.Adapter<ContactViewHolder>() {

    private var contacts = mutableListOf<ContactModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position], glide)
        holder.clickListener(clickListener)
    }

    override fun getItemCount(): Int = contacts.size

    fun addItems(data: List<ContactModel>) {
        val result = DiffUtil.calculateDiff(ContactsDiffUtilCallback(contacts, data))
        contacts.apply {
            clear()
            addAll(data)
            sortBy { it.full_name }
        }
        result.dispatchUpdatesTo(this)
    }

}