package st.slex.messenger.ui.chats

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.auth.core.Resource
import st.slex.messenger.ui.core.ClickListener
import kotlin.reflect.KSuspendFunction1

class ChatsAdapter(
    options: FirebaseRecyclerOptions<ChatsUI>,
    private val itemClick: ClickListener<ChatsUI>,
    private val kSuspendFunction: KSuspendFunction1<ChatsUI, SharedFlow<Resource<ChatsUI>>>,
    private val lifecycleScope: LifecycleCoroutineScope
) : FirebaseRecyclerAdapter<ChatsUI, ChatsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return ChatsViewHolder.Base(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int, model: ChatsUI) {
        lifecycleScope.launch {
            kSuspendFunction.invoke(model).collect {
                launch(Dispatchers.Main) {
                    when (it) {
                        is Resource.Success -> {
                            Log.i("TAG", it.data.toString())
                            holder.bind(it.data)
                        }
                        is Resource.Failure -> {
                            Log.i("TAG Resource.Failure", it.exception.toString())
                            holder.bindError(it.exception.message.toString())
                        }
                        is Resource.Loading -> {
                            holder.bindLoading()
                        }
                    }
                }
            }
        }
    }
}