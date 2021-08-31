package st.slex.messenger.ui.main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.utilites.result.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainScreenViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    val currentUser: LiveData<Response<UserModel>> = liveData(Dispatchers.IO) {
        emit(Response.Loading)
        try {
            repository.getCurrentUser().collect {
                emit(it)
            }
        } catch (exception: Exception) {
            emit(Response.Failure(exception = exception))
        }
    }
}