package st.slex.messenger.main.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.core.Resource
import st.slex.messenger.main.databinding.ItemRecyclerContactBinding
import st.slex.messenger.main.ui.core.ClickListener
import st.slex.messenger.main.ui.user_profile.UserUI

class ContactAdapter(
    private val clickListener: ClickListener<ContactUI>,
    private val kSuspendFunction: suspend (String) -> StateFlow<Resource<UserUI>>,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<ContactViewHolder>() {

    private val contacts = mutableListOf<ContactUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        lifecycleScope.launch(Dispatchers.IO) {
            kSuspendFunction.invoke(contact.getId).collect { result ->
                if (result is Resource.Success) {
                    launch(Dispatchers.Main) {
                        holder.bind(contact.copy(url = result.data.url()))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun setItems(items: List<ContactUI>) {
        val diffUtil = ContactsDiffUtilCallback(contacts, items)
        val calculatedResult = DiffUtil.calculateDiff(diffUtil)
        contacts.clear()
        contacts.addAll(items)
        calculatedResult.dispatchUpdatesTo(this)
    }
}