package st.slex.messenger.ui.main_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.utilites.base.CardClickListener

class MainAdapter(private val clickListener: CardClickListener) :
    RecyclerView.Adapter<MainViewHolder>() {

    private var list = mutableListOf<ChatListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(list[position])
        holder.clickListener(clickListener)
    }

    override fun getItemCount(): Int = list.size

    fun addChat(message: ChatListModel) {
        if (!list.contains(message)) {
            var update = false
            list.map {
                if (it.id == message.id) {
                    update = true
                    message
                } else it
            }
            if (!update) list.add(message)
            list.apply {
                sortBy { it.timestamp.toString() }
                reverse()
            }
            notifyDataSetChanged()
        }
    }
}