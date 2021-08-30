package st.slex.messenger.ui.single_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.ChildEventResponse
import st.slex.messenger.utilites.result.Response
import st.slex.messenger.utilites.result.ValueEventResponse
import javax.inject.Inject

class SingleChatViewModel @Inject constructor(private val repository: SingleChatRepository) :
    ViewModel() {

    fun getStatus(uid: String): LiveData<Response<String>> = liveData(Dispatchers.IO) {
        emit(Response.Loading)
        try {
            repository.getStatus(uid = uid).collect {
                when (it) {
                    is ValueEventResponse.Success -> emit(Response.Success(it.snapshot.getThisValue<String>()))
                    is ValueEventResponse.Cancelled -> emit(Response.Failure(it.databaseError.toException()))
                }
            }
        } catch (exception: Exception) {
            emit(Response.Failure(exception))
        }
    }

    fun getMessages(limitToLast: Int): LiveData<Response<MessageModel>> = liveData(Dispatchers.IO) {
        emit(Response.Loading)
        try {
            repository.getMessages(limitToLast = limitToLast).collect {
                when (it) {
                    is ChildEventResponse.Added -> {
                        Response.Success(it.snapshot.getThisValue<MessageModel>())
                    }
                    is ChildEventResponse.Moved -> {
                    }
                    is ChildEventResponse.Changed -> {
                    }
                    is ChildEventResponse.Removed -> {
                    }
                    is ChildEventResponse.Cancelled -> {
                        Response.Failure(it.databaseError.toException())
                    }
                }
            }
        } catch (exception: Exception) {
            emit(Response.Failure(exception = exception))
        }
    }

    fun sendMessage(message: String, uid: String) = viewModelScope.launch {
        repository.sendMessage(message = message, uid = uid)
    }

}