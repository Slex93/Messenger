package st.slex.messenger.ui.main_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

    private var mainList = mutableListOf<ChatListModel>()
    private val mapList = mutableMapOf<String, ChatListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = mainList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mainList.size

    fun makeMainList(message: ChatListModel) {
        mapList[message.user.id] = message
        mainList = mapList.values.toMutableList()
        notifyDataSetChanged()
    }
}