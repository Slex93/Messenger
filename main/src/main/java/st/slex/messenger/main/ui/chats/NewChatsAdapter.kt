package st.slex.messenger.main.ui.chats

import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import st.slex.messenger.core.Resource
import st.slex.messenger.main.ui.core.ClickListener
import kotlin.reflect.KSuspendFunction1

class NewChatsAdapter(
    private val itemClick: ClickListener<ChatsUI>,
    private val kSuspendFunction: KSuspendFunction1<ChatsUI, StateFlow<Resource<ChatsUI>>>,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<ChatsViewHolder>() {

    private val _chats = mutableListOf<ChatsUI>()
    private val chats: List<ChatsUI> get() = _chats.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = chats.size

    fun setItems(items: List<ChatsUI>) {
        _chats.clear()
        _chats.addAll(items)
    }
}