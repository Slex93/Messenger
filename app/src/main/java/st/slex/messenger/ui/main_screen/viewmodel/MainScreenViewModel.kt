package st.slex.messenger.ui.main_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import st.slex.messenger.data.repository.impl.MainRepositoryImpl
import st.slex.messenger.ui.main_screen.model.base.MainMessage

class MainScreenViewModel(private val repository: MainRepositoryImpl) : ViewModel() {
    val currentUser = repository.currentUser
    val mainMessage: LiveData<List<MainMessage>> = repository.getTestList()
    fun getCurrentUser() = viewModelScope.launch {
        repository.getCurrentUser()
    }
}