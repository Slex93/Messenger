package st.slex.messenger.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.messenger.data.chat.MessageModel

class MessagesAdapter(
    options: DatabasePagingOptions<MessageModel>,
    private val id: String
) : FirebaseRecyclerPagingAdapter<MessageModel, MessagesViewHolder>(options) {

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int, model: MessageModel) {
        if (model.from == id) holder.bindUser(model)
        else holder.bindReceiver(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder =
        MessagesViewHolder(
            ItemRecyclerSingleChatBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
}