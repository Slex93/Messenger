package st.slex.messenger.ui.single_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.repository.interf.SingleChatRepository
import st.slex.messenger.utilites.getThisValue
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource

class SingleChatViewModel(private val repository: SingleChatRepository) : ViewModel() {

    fun initStatus(uid: String): LiveData<Resource<String>> = liveData(Dispatchers.IO) {
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

    private inline fun <reified T> collector(): FlowCollector<T> = object : FlowCollector<T> {
        override suspend fun emit(value: T) {
            when (value) {
                is EventResponse.Success -> Resource.Success(value.snapshot.getThisValue<T>())
                is EventResponse.Cancelled -> Resource.Failure(value.databaseError.toException())
            }
        }
    }

    fun initMessage(uid: String, countMessage: Int) {
        repository.initMessages(uid, countMessage)
    }

    fun sendMessage(message: String, uid: String) {
        repository.sendMessage(message, uid)
    }

}