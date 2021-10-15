package st.slex.messenger.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import st.slex.common.messenger.databinding.ItemRecyclerContactBinding
import st.slex.messenger.core.Resource
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.ui.user_profile.UserUI

class ContactAdapter(
    options: FirebaseRecyclerOptions<ContactsUI>,
    private val clickListener: ClickListener<ContactsUI>,
    private val kSuspendFunction: suspend (String) -> StateFlow<Resource<UserUI>>,
    private val lifecycleScope: LifecycleCoroutineScope,
) : FirebaseRecyclerAdapter<ContactsUI, ContactViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerContactBinding.inflate(inflater, parent, false)
        return ContactViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int, model: ContactsUI) {
        lifecycleScope.launch(Dispatchers.IO) {
            kSuspendFunction.invoke(model.getId).collectLatest { result ->
                launch(Dispatchers.Main) {
                    when (result) {
                        is Resource.Success -> holder.bind(model.copy(url = result.data?.getUrl))

                        is Resource.Failure -> {

                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }
    }
}