package st.slex.messenger.main.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.core.Resource
import st.slex.messenger.main.databinding.ItemRecyclerContactBinding
import st.slex.messenger.main.ui.core.ClickListener
import st.slex.messenger.main.ui.user_profile.UserUI

class ContactAdapter(
    options: FirebaseRecyclerOptions<ContactUI>,
    private val clickListener: ClickListener<ContactUI>,
    private val kSuspendFunction: suspend (String) -> StateFlow<Resource<UserUI>>,
    private val lifecycleScope: LifecycleCoroutineScope,
) : FirebaseRecyclerAdapter<ContactUI, ContactViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int, model: ContactUI) {
        lifecycleScope.launch(Dispatchers.IO) {
            kSuspendFunction.invoke(model.getId).collect { result ->
                if (result is Resource.Success) {
                    val url = result.data.url()
                    launch(Dispatchers.Main) {
                        holder.bind(model.copy(url = result.data.url()))
                    }
                }
            }
        }
    }
}