package st.slex.messenger.ui.main_screen.adapter

import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.base.SetImageWithGlide
import st.slex.messenger.utilites.funs.convertToTime

class MainViewHolder(private val binding: ItemRecyclerMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var _url: String? = null
    private val url get() = _url!!

    fun bind(item: ChatListModel, glide: SetImageWithGlide) {
        _url = item.url
        val name = if (item.full_name.isEmpty()) {
            item.username
        } else item.full_name
        binding.itemMainCard.transitionName = item.id
        binding.itemMainUsername.text = name
        binding.itemMainContent.text = item.text
        binding.itemMainTimestamp.text = item.timestamp.toString().convertToTime()
        glide.setImage(
            binding.itemMainImage,
            url,
            needCircleCrop = true,
            needCrop = true
        )
    }

    fun clickListener(clickListener: CardClickListener) {
        binding.itemMainCard.setOnClickListener {
            clickListener.onClick(it, url)
        }
    }


}