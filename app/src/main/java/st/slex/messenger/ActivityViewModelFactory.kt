package st.slex.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import st.slex.messenger.data.repository.impl.ActivityRepositoryImpl

class ActivityViewModelFactory(private val repository: ActivityRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}