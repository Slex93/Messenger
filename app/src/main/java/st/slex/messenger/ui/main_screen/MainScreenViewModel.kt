package st.slex.messenger.ui.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.utilites.funs.getThisValue
import st.slex.messenger.utilites.result.Response
import st.slex.messenger.utilites.result.ValueEventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainScreenViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    val mainMessage: LiveData<List<MessageModel>> = liveData(Dispatchers.IO) {
        repository.getTestList().collect {
            emit(it)
        }
    }

    val currentUser: LiveData<Response<UserModel>> = liveData(Dispatchers.IO) {
        repository.getCurrentUser().collect {
            Response.Loading
            try {
                repository.getCurrentUser().collect {
                    when (it) {
                        is ValueEventResponse.Success -> {
                            emit(Response.Success(it.snapshot.getThisValue<UserModel>()))
                        }
                        is ValueEventResponse.Cancelled -> {
                            emit(Response.Failure(it.databaseError.toException()))
                        }
                    }
                }
            } catch (exception: Exception) {
                Response.Failure(exception = exception)
            }
        }
    }
}