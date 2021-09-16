package st.slex.messenger.ui.chats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.ui.chats.ChatsDiffUtilCallback
import st.slex.messenger.ui.chats.ChatsUI
import st.slex.messenger.ui.core.ClickListener

class ChatsAdapter(
    private val clickListener: ClickListener<ChatsUI>,
) : RecyclerView.Adapter<ChatsViewHolder>() {

    private var chats = mutableListOf<ChatsUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return ChatsViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int = chats.size

    fun addChat(data: List<ChatsUI>) {
        val result = DiffUtil.calculateDiff(ChatsDiffUtilCallback(chats, data))
        chats.clear()
        chats.addAll(data)
        result.dispatchUpdatesTo(this)
    }
}