package st.slex.messenger.main_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import st.slex.messenger.main_screen.model.MainScreenRepository
import st.slex.messenger.main_screen.model.base.MainMessage

class MainScreenViewModel(repository: MainScreenRepository) : ViewModel() {

    val mainMessage: LiveData<List<MainMessage>> = repository.mainMessage

}