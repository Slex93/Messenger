package st.slex.messenger.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import st.slex.messenger.data.chat.MessageModel

class SingleChatAdapter(
    options: FirebaseRecyclerOptions<MessageModel>
) : FirebaseRecyclerAdapter<MessageModel, SingleChatViewHolder>(options) {

    override fun onBindViewHolder(
        holder: SingleChatViewHolder,
        position: Int,
        model: MessageModel
    ) {
        if (model.from == Firebase.auth.uid.toString()) holder.bindUser(model)
        else holder.bindReceiver(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder =
        SingleChatViewHolder(
            ItemRecyclerSingleChatBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
}