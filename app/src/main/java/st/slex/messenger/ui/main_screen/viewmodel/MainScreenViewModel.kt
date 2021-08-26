package st.slex.messenger.ui.main_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.impl.MainRepositoryImpl
import st.slex.messenger.utilites.getThisValue
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource

@ExperimentalCoroutinesApi
class MainScreenViewModel(private val repository: MainRepositoryImpl) : ViewModel() {

    val mainMessage: LiveData<List<MessageModel>> = liveData(Dispatchers.IO) {
        repository.getTestList().collect {
            emit(it)
        }
    }

    val currentUser: LiveData<Resource<UserModel>> = liveData(Dispatchers.IO) {
        repository.getCurrentUser().collect {
            Resource.Loading
            try {
                repository.getCurrentUser().collect {
                    when (it) {
                        is EventResponse.Success -> {
                            emit(Resource.Success(it.snapshot.getThisValue<UserModel>()))
                        }
                        is EventResponse.Cancelled -> {
                            emit(Resource.Failure(it.databaseError.toException()))
                        }
                    }
                }
            } catch (exception: Exception) {
                Resource.Failure(exception = exception)
            }
        }
    }
}