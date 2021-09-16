package st.slex.messenger.ui.main_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.ui.core.ClickListener
import st.slex.messenger.ui.main_screen.ChatsUI
import st.slex.messenger.ui.single_chat.adapter.ChatsDiffUtilCallback

class MainAdapter(
    private val clickListener: ClickListener<ChatsUI>,
) : RecyclerView.Adapter<MainViewHolder>() {

    private var chats = mutableListOf<ChatsUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return MainViewHolder.Base(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
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