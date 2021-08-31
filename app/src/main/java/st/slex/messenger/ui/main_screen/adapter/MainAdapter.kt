package st.slex.messenger.ui.main_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.data.model.ChatListModel
import st.slex.messenger.utilites.base.CardClickListener

class MainAdapter(private val clickListener: CardClickListener) :
    RecyclerView.Adapter<MainViewHolder>() {

    private var mainList = mutableListOf<ChatListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(mainList[position])
        holder.clickListener(clickListener)
    }

    override fun getItemCount(): Int = mainList.size

    fun makeMainList(message: ChatListModel) {
        if (!mainList.contains(message)) mainList.add(message)
        notifyItemInserted(mainList.size)
    }
}