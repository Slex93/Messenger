package st.slex.messenger.main.ui.chats

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
import st.slex.messenger.main.databinding.ItemRecyclerMainBinding
import st.slex.messenger.main.ui.core.ClickListener
import kotlin.reflect.KSuspendFunction1

class ChatsAdapter(
    options: FirebaseRecyclerOptions<ChatsUI>,
    private val itemClick: ClickListener<ChatsUI>,
    private val kSuspendFunction: KSuspendFunction1<ChatsUI, StateFlow<Resource<ChatsUI>>>,
    private val lifecycleScope: LifecycleCoroutineScope
) : FirebaseRecyclerAdapter<ChatsUI, ChatsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return ChatsViewHolder.Base(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int, model: ChatsUI) {
        lifecycleScope.launchWhenResumed {
            kSuspendFunction.invoke(model).collect {
                launch(Dispatchers.Main) { bindState(holder, it) }
            }
        }
    }

    private fun bindState(holder: ChatsViewHolder, result: Resource<ChatsUI>) =
        when (result) {
            is Resource.Success -> {
                holder.bind(result.data)
            }
            is Resource.Failure -> {
                holder.bindError(result.exception.message.toString())
            }
            is Resource.Loading -> {
                holder.bindLoading()
            }
        }
}