package st.slex.messenger.ui.main_screen.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.MessageModel

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MessageModel) {
        binding.itemMainUsername.text = item.from
        binding.itemMainContent.text = item.text
        binding.itemMainTimestamp.text = item.timestamp.toString()
    }

}