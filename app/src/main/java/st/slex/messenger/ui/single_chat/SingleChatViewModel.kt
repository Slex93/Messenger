package st.slex.messenger.ui.single_chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

class SingleChatViewModel @Inject constructor(private val repository: SingleChatRepository) :
    ViewModel() {

    fun getUser(uid: String): LiveData<Response<UserModel>> = liveData(Dispatchers.IO) {
        emit(Response.Loading)
        try {
            repository.getUser(uid = uid).collect {
                emit(it)
            }
        } catch (exception: Exception) {
            emit(Response.Failure(exception))
        }
    }

    fun getMessages(uid: String, limitToLast: Int): LiveData<Response<MessageModel>> =
        liveData(Dispatchers.IO) {
            emit(Response.Loading)
            try {
                repository.getMessages(uid = uid, limitToLast = limitToLast).collect {
                    emit(it)
                }
            } catch (exception: Exception) {
                emit(Response.Failure(exception = exception))
            }
        }

    fun sendMessage(message: String, user: UserModel) = viewModelScope.launch {
        repository.getCurrentUser(user.id).collect {
            when (it) {
                is Response.Success -> {
                    repository.sendMessage(message = message, user = user, it.value)
                }
                is Response.Failure -> {
                    Log.e(
                        "Failure in SingleChatViewModel:",
                        it.exception.message.toString(),
                        it.exception.fillInStackTrace()
                    )
                }
            }
        }
    }

}