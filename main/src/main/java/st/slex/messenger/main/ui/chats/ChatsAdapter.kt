package st.slex.messenger.main.ui.chats

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import st.slex.core.Resource
import st.slex.messenger.main.databinding.ItemRecyclerMainBinding
import st.slex.messenger.main.ui.core.ClickListener

class ChatsAdapter(
    private val itemClick: ClickListener<ChatsUI>
) : RecyclerView.Adapter<ChatsViewHolder>() {

    private val _chats = mutableListOf<Resource<ChatsUI>>()
    private val chats: List<Resource<ChatsUI>> get() = _chats.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerMainBinding.inflate(inflater, parent, false)
        return ChatsViewHolder.Base(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bindState(chats[position])
    }

    private fun ChatsViewHolder.bindState(result: Resource<ChatsUI>) = when (result) {
        is Resource.Success -> bind(result.data)
        is Resource.Failure -> bindError(result.exception.message.toString())
        is Resource.Loading -> bindLoading()
    }

    override fun getItemCount(): Int = chats.size

    fun setItems(items: List<Resource<ChatsUI>>) {
        Log.i(TAG, items.toString())
        val diffUtil = ChatsDiffUtilCallback(chats, items)
        val result = DiffUtil.calculateDiff(diffUtil)
        _chats.clear()
        _chats.addAll(items)
        result.dispatchUpdatesTo(this)
    }
}