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
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource
import javax.inject.Inject

class SingleChatViewModel @Inject constructor(private val repository: SingleChatRepository) :
    ViewModel() {

    fun getStatus(uid: String): LiveData<Resource<String>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            repository.getStatus(uid = uid).collect {
                when (it) {
                    is EventResponse.Success -> emit(Resource.Success(it.snapshot.getThisValue<String>()))
                    is EventResponse.Cancelled -> emit(Resource.Failure(it.databaseError.toException()))
                }
            }
        } catch (exception: Exception) {
            emit(Resource.Failure(exception))
        }
    }

    fun getMessages(limitToLast: Int): LiveData<Resource<MessageModel>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            repository.getMessages(limitToLast = limitToLast).collect {
                when (it) {
                    is ChildEventResponse.Added -> {
                        Resource.Success(it.snapshot.getThisValue<MessageModel>())
                    }
                    is ChildEventResponse.Moved -> {
                    }
                    is ChildEventResponse.Changed -> {
                    }
                    is ChildEventResponse.Removed -> {
                    }
                    is ChildEventResponse.Cancelled -> {
                        Resource.Failure(it.databaseError.toException())
                    }
                }
            }
        } catch (exception: Exception) {
            emit(Resource.Failure(exception = exception))
        }
    }

    fun sendMessage(message: String, uid: String) = viewModelScope.launch {
        repository.sendMessage(message = message, uid = uid)
    }

}