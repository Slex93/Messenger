package st.slex.messenger.ui.main_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.repository.impl.MainRepositoryImpl
import st.slex.messenger.ui.main_screen.model.base.MainMessage
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.result.Resource

@ExperimentalCoroutinesApi
class MainScreenViewModel(private val repository: MainRepositoryImpl) : ViewModel() {
    val mainMessage: LiveData<List<MainMessage>> = repository.getTestList()

    val currentUser: LiveData<EventResponse> = liveData(Dispatchers.IO) {
        repository.getCurrentUser().collect {
            try {
                repository.getCurrentUser().collect {
                    emit(it)
                }
            } catch (exception: Exception) {
                Resource.Failure(exception = exception)
            }
        }
    }
}