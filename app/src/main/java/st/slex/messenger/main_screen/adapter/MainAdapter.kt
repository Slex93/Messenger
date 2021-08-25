package st.slex.messenger.main_screen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import st.slex.common.messenger.databinding.ItemRecyclerMainBinding
import st.slex.messenger.main_screen.model.base.MainMessage

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

    private var mainList = mutableListOf<MainMessage>()

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

    fun makeMainList(message: MutableList<MainMessage>) {
        mainList = message
        notifyDataSetChanged()
    }
}