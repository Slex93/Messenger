package st.slex.messenger.ui.main_screen.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatListModel) {
        binding.itemMainUsername.text = item.user.username
        binding.itemMainContent.text = item.message.text
        binding.itemMainTimestamp.text = item.message.timestamp.toString()
    }

}