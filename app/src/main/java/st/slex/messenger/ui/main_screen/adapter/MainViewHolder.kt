package st.slex.messenger.ui.main_screen.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.MessageModel

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MessageModel) {
        binding.itemMainUsername.text = item.username
        binding.itemMainContent.text = item.content
        binding.itemMainTimestamp.text = item.timestamp
    }

}