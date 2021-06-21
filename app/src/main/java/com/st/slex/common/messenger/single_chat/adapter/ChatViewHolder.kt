package com.st.slex.common.messenger.single_chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.st.slex.common.messenger.databinding.ItemRecyclerSingleChatBinding
import com.st.slex.common.messenger.single_chat.model.Message
import com.st.slex.common.messenger.utilites.asTime
import java.text.SimpleDateFormat
import java.util.*

class ChatViewHolder(private val binding: ItemRecyclerSingleChatBinding):RecyclerView.ViewHolder(binding.root) {

    fun bindUser(message: Message){
        binding.chatBlockUserMessage.visibility = View.VISIBLE
        binding.chatBlockReceiverMessage.visibility = View.GONE
        binding.chatUserMessage.text = message.text
        binding.chatUserMessageTime.text = message.timestamp.toString().asTime()
    }

    fun bindReceiver(message: Message){
        binding.chatBlockReceiverMessage.visibility = View.VISIBLE
        binding.chatBlockUserMessage.visibility = View.GONE
        binding.chatReceiverMessage.text = message.text
        binding.chatReceiverMessageTime.text = message.timestamp.toString().asTime()
    }

}
