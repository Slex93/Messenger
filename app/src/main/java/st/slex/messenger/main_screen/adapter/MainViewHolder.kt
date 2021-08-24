package st.slex.messenger.main_screen.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.common.messenger.main_screen.model.base.MainMessage

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainMessage) {
        binding.itemMainUsername.text = item.username
        binding.itemMainContent.text = item.content
        binding.itemMainTimestamp.text = item.timestamp
    }

}