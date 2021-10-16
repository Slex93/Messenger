package st.slex.messenger.ui.single_chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding

class SingleChatAdapter(
    options: FirebaseRecyclerOptions<MessageUI>
) : FirebaseRecyclerAdapter<MessageUI, SingleChatViewHolder>(options) {

    override fun onBindViewHolder(
        holder: SingleChatViewHolder,
        position: Int,
        model: MessageUI
    ) {
        if (model.getId() == Firebase.auth.uid.toString()) holder.bindUser(model)
        else holder.bindReceiver(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerSingleChatBinding.inflate(inflater, parent, false)
        return SingleChatViewHolder.Base(binding)
    }
}