package st.slex.messenger.ui.main_screen.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.funs.convertToTime

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    private lateinit var clickListener: CardClickListener

    fun bind(item: ChatListModel) {
        binding.itemMainCard.transitionName = item.id
        binding.itemMainUsername.text = item.full_name
        binding.itemMainContent.text = item.text
        binding.itemMainTimestamp.text = item.timestamp.toString().convertToTime()
    }

    fun clickListener(clickListener: CardClickListener) {
        this.clickListener = clickListener
        binding.itemMainCard.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        clickListener.onClick(binding.itemMainCard)
    }


}