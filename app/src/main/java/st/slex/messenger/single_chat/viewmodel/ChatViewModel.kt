package st.slex.messenger.single_chat.viewmodel

import androidx.lifecycle.ViewModel
import st.slex.messenger.single_chat.model.ChatRepository

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {

    val status = repository.status
    val message = repository.message

    fun initStatus(uid: String) {
        repository.initStatus(uid)
    }

    fun initMessage(uid: String, countMessage: Int) {
        repository.initMessages(uid, countMessage)
    }

    fun sendMessage(message: String, uid: String) {
        repository.sendMessage(message, uid)
    }

}