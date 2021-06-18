package com.st.slex.common.messenger.main_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.st.slex.common.messenger.main_screen.model.MainScreenRepository
import com.st.slex.common.messenger.main_screen.model.base.MainMessage

class MainScreenViewModel(repository: MainScreenRepository) : ViewModel() {

    val mainMessage: LiveData<List<MainMessage>> = repository.mainMessage

}