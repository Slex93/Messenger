package st.slex.messenger.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import st.slex.messenger.data.repository.interf.SettingsRepository
import st.slex.messenger.utilites.result.VoidResponse
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    fun signOut(state: String): LiveData<VoidResponse> = liveData(Dispatchers.IO) {
        emit(VoidResponse.Loading)
        try {
            repository.signOut(state).collect {
                emit(it)
            }
        } catch (exception: Exception) {
            emit(VoidResponse.Failure(exception))
        }
    }
}