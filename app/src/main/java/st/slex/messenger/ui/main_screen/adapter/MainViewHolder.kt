package st.slex.messenger.ui.main_screen.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.ui.main_screen.ChatsUI
import st.slex.messenger.utilites.base.CardClickListener
import st.slex.messenger.utilites.base.SetImageWithGlide

abstract class MainViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    open fun bind(item: ChatsUI) {

    }

    class Base(
        private val binding: ItemRecyclerMainBinding,
        private val clickListener: CardClickListener,
        private val glide: SetImageWithGlide
    ) : MainViewHolder(binding.root) {
        override fun bind(item: ChatsUI) = with(binding) {
            item.map(
                userName = itemMainUsername,
                userAvatar = itemMainImage
            )
        }
    }

    private var _url: String? = null
    private val url get() = _url!!

    open fun bind(item: ChatsUI, glide: SetImageWithGlide) {

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