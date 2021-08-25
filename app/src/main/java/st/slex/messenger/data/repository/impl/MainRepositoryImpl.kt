package st.slex.messenger.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.ui.main_screen.model.base.MainMessage
import st.slex.messenger.utilites.Const.CURRENT_UID
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT
import st.slex.messenger.utilites.result.EventResponse
import st.slex.messenger.utilites.valueEventFlow

@ExperimentalCoroutinesApi
class MainRepositoryImpl : MainRepository {

    suspend fun getCurrentUser(): Flow<EventResponse> =
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID).valueEventFlow()

    fun getTestList(): LiveData<List<MainMessage>> = liveData {
        this.emit(
            listOf(
                MainMessage("1", "Helen", "Hello, Alex", "12:30"),
                MainMessage("1", "Bob", "Wow", "11:30"),
                MainMessage("1", "Anya", "I don't know", "10:30"),
                MainMessage("1", "Olga", "May be it is not a good idea", "09:30")
            )
        )
    }

}